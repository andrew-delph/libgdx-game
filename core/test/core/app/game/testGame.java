package core.app.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Clock;
import core.common.CommonFactory;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.StandAloneConfig;
import core.entity.ActiveEntityManager;
import core.entity.controllers.factories.EntityControllerFactory;
import core.mock.GdxTestRunner;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class testGame {

  Injector injector;
  Game game;
  Clock clock;
  GameStore gameStore;
  ActiveEntityManager activeEntityManager;
  GameController gameController;
  EntityControllerFactory entityControllerFactory;

  @Before
  public void setup()
      throws IOException, WrongVersion, SerializationDataMissing, InterruptedException,
          BodyNotFound {
    injector = Guice.createInjector(new StandAloneConfig());
    game = injector.getInstance(Game.class);
    clock = injector.getInstance(Clock.class);
    gameStore = injector.getInstance(GameStore.class);
    activeEntityManager = injector.getInstance(ActiveEntityManager.class);
    gameController = injector.getInstance(GameController.class);
    entityControllerFactory = injector.getInstance(EntityControllerFactory.class);
    game.start();
  }

  @Test
  public void testChunksRunningStandAlone() throws InterruptedException, ChunkNotFound {
    clock.waitForTick(50); // TODO should change to wait for generation to be complete
    AtomicReference<Boolean> check = new AtomicReference<>(false);
    clock.waitForTick(
        () -> {
          check.set(gameStore.getChunkOnClock(this.clock.getCurrentTick()).size() == 0);
        });
    assert check.get();
    gameController.createEntity(
        CommonFactory.createCoordinates(0, 0),
        (entity ->
            entity.setEntityController(
                entityControllerFactory.createRemoteBodyController(entity))));
    clock.waitForTick(
        3,
        () -> {
          System.out.println("1:" + gameStore.getChunkOnClock(this.clock.getCurrentTick()).size());
          check.set(gameStore.getChunkOnClock(this.clock.getCurrentTick()).size() == 1);
        });
    assert check.get();
    clock.waitForTick(
        () -> {
          System.out.println("2:" + gameStore.getChunkOnClock(this.clock.getCurrentTick()).size());
          check.set(gameStore.getChunkOnClock(this.clock.getCurrentTick()).size() == 1);
        });
    assert check.get();
  }
}

package app.game;

import app.GameController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Clock;
import common.Coordinates;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.StandAloneConfig;
import entity.ActiveEntityManager;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.mock.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class testGame {

  Injector injector;
  Game game;
  Clock clock;
  GameStore gameStore;
  ActiveEntityManager activeEntityManager;
  GameController gameController;

  @Before
  public void setup()
      throws IOException, WrongVersion, SerializationDataMissing, InterruptedException {
    injector = Guice.createInjector(new StandAloneConfig());
    game = injector.getInstance(Game.class);
    clock = injector.getInstance(Clock.class);
    gameStore = injector.getInstance(GameStore.class);
    activeEntityManager = injector.getInstance(ActiveEntityManager.class);
    gameController = injector.getInstance(GameController.class);
    game.start();
  }

  @Test
  public void testChunksRunningStandAlone() throws InterruptedException, ChunkNotFound {
    clock.waitForTick(4);
    AtomicReference<Boolean> check = new AtomicReference<>(false);
    clock.waitForTick(
        () -> {
          check.set(gameStore.getChunkOnClock(this.clock.getCurrentTick()).size() == 0);
        });
    assert check.get();
    gameController.createEntity(new Coordinates(0, 0));
    clock.waitForTick(
        3,
        () -> {
          check.set(gameStore.getChunkOnClock(this.clock.getCurrentTick()).size() == 1);
        });
    assert check.get();
    clock.waitForTick(
        () -> {
          check.set(gameStore.getChunkOnClock(this.clock.getCurrentTick()).size() == 1);
        });
    assert check.get();
  }
}

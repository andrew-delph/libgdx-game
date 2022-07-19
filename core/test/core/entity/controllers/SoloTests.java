package core.entity.controllers;

import core.app.game.Game;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.Clock;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.StandAloneConfig;
import core.entity.Entity;
import core.entity.attributes.msc.Coordinates;
import core.entity.attributes.msc.Health;
import core.entity.controllers.factories.EntityControllerFactory;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class SoloTests {
  Injector injector;
  Game game;
  GameController gameController;
  GameStore gameStore;
  EntityControllerFactory entityControllerFactory;

  Clock clock;

  @Before
  public void setup()
      throws WrongVersion, SerializationDataMissing, IOException, InterruptedException,
          BodyNotFound {
    injector = Guice.createInjector(new StandAloneConfig());

    game = injector.getInstance(Game.class);
    gameController = injector.getInstance(GameController.class);
    gameStore = injector.getInstance(GameStore.class);
    clock = injector.getInstance(Clock.class);
    entityControllerFactory = injector.getInstance(EntityControllerFactory.class);

    game.start();
  }

  @Test
  public void testUpdateHealthKill() throws ChunkNotFound, EntityNotFound, InterruptedException {

    Entity entity = gameController.createEntity(new Coordinates(0, 0));
    entity.setEntityController(entityControllerFactory.createEntityUserController(entity));

    Health newHealth = new Health(-100);

    gameController.updateEntityAttribute(entity.getUuid(), newHealth);

    clock.waitForTick();

    assert !gameStore.doesEntityExist(entity.getUuid());
  }
}

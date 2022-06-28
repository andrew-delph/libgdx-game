package entity.controllers;

import app.GameController;
import app.game.Game;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Clock;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.StandAloneConfig;
import entity.Entity;
import entity.attributes.msc.Coordinates;
import entity.attributes.msc.Health;
import entity.controllers.factories.EntityControllerFactory;
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

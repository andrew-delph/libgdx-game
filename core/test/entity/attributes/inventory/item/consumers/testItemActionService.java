package entity.attributes.inventory.item.consumers;

import app.game.Game;
import app.game.GameController;
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
import entity.attributes.inventory.item.ItemActionType;
import entity.attributes.inventory.item.comsumers.ItemActionService;
import entity.attributes.msc.Coordinates;
import entity.controllers.factories.EntityControllerFactory;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class testItemActionService {

  Injector injector;
  Game game;
  GameController gameController;
  GameStore gameStore;
  EntityControllerFactory entityControllerFactory;
  ItemActionService itemActionService;

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
    itemActionService = injector.getInstance(ItemActionService.class);

    game.start();
  }

  @Test
  public void testActionItemService() throws ChunkNotFound, EntityNotFound, InterruptedException {

    Entity entity1 = gameController.createEntity(new Coordinates(0, 0));
    Entity entity2 = gameController.createEntity(new Coordinates(1, 0));

    assert entity2.getHealth().getValue() == 100;

    itemActionService.use(ItemActionType.DEFAULT, entity1.getUuid());

    assert entity2.getHealth().getValue() < 100;
  }
}

package core.entity.attributes.inventory.item.consumers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.game.Game;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Clock;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.StandAloneConfig;
import core.entity.Entity;
import core.entity.attributes.inventory.item.ItemActionType;
import core.entity.attributes.inventory.item.comsumers.ItemActionService;
import core.entity.attributes.msc.Coordinates;
import core.entity.controllers.factories.EntityControllerFactory;
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
    assert entity1.getHealth().getValue() == 100;
  }
}

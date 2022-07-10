package entity.attributes.inventory;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import app.game.Game;
import app.game.GameController;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;
import common.Clock;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.StandAloneConfig;
import entity.Entity;
import entity.attributes.inventory.item.comsumers.ItemActionService;
import entity.attributes.msc.Coordinates;
import entity.controllers.factories.EntityControllerFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ItemActionSoloTests {
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

    injector =
        Guice.createInjector(
            Modules.override(new StandAloneConfig())
                .with(
                    new AbstractModule() {
                      @Override
                      protected void configure() {
                        ItemActionService itemActionService = Mockito.spy(new ItemActionService());
                        bind(ItemActionService.class).toProvider(Providers.of(itemActionService));
                      }
                    }));

    game = injector.getInstance(Game.class);
    gameController = injector.getInstance(GameController.class);
    gameStore = injector.getInstance(GameStore.class);
    clock = injector.getInstance(Clock.class);
    entityControllerFactory = injector.getInstance(EntityControllerFactory.class);
    itemActionService = injector.getInstance(ItemActionService.class);

    injector.injectMembers(itemActionService);

    game.start();
  }

  @Test
  public void testActionItemService() throws ChunkNotFound, EntityNotFound, InterruptedException {

    doNothing().when(itemActionService).use(anyObject(), anyObject());

    Entity entity1 = gameController.createEntity(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    gameController.useItem(entity1);
    gameController.useItem(entity1);

    TimeUnit.SECONDS.sleep(1);

    verify(itemActionService, times(1)).use(anyObject(), anyObject());
  }
}

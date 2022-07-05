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
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.Entity;
import entity.attributes.inventory.item.comsumers.ItemActionService;
import entity.attributes.msc.Coordinates;
import entity.controllers.factories.EntityControllerFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import util.mock.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class ItemActionMultiplayerTests {

  Injector clientInjector;
  Injector serverInjector;

  ClientNetworkHandle clientNetworkHandle;
  ServerNetworkHandle serverNetworkHandle;

  GameStore serverGameStore;
  GameStore clientGameStore;

  GameController serverGameController;
  GameController clientGameController;

  ItemActionService serverItemActionService;
  ItemActionService clientItemActionService;

  Game clientGame, serverGame;

  EntityControllerFactory clientEntityControllerFactory;

  @Before
  public void setup()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
          BodyNotFound {
    clientInjector =
        Guice.createInjector(
            Modules.override(new ClientConfig())
                .with(
                    new AbstractModule() {
                      @Override
                      protected void configure() {
                        ItemActionService itemActionService = Mockito.spy(new ItemActionService());
                        bind(ItemActionService.class).toProvider(Providers.of(itemActionService));
                      }
                    }));

    serverInjector =
        Guice.createInjector(
            Modules.override(new BaseServerConfig())
                .with(
                    new AbstractModule() {
                      @Override
                      protected void configure() {
                        ItemActionService itemActionService = Mockito.spy(new ItemActionService());
                        bind(ItemActionService.class).toProvider(Providers.of(itemActionService));
                      }
                    }));

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);

    clientGame = clientInjector.getInstance(Game.class);
    serverGame = serverInjector.getInstance(Game.class);

    clientGameController = clientInjector.getInstance(GameController.class);
    serverGameController = serverInjector.getInstance(GameController.class);

    serverGameStore = serverInjector.getInstance(GameStore.class);
    clientGameStore = clientInjector.getInstance(GameStore.class);

    serverItemActionService = serverInjector.getInstance(ItemActionService.class);
    clientItemActionService = clientInjector.getInstance(ItemActionService.class);

    clientEntityControllerFactory = clientInjector.getInstance(EntityControllerFactory.class);

    serverGame.start();
    clientGame.start();
  }

  @After
  public void cleanup() {
    try {
      clientGame.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      serverGame.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void TestClientUsesDefaultItem()
      throws ChunkNotFound, InterruptedException, EntityNotFound {

    doNothing().when(serverItemActionService).use(anyObject(), anyObject());
    doNothing().when(clientItemActionService).use(anyObject(), anyObject());

    Entity entity1 = serverGameController.createEntity(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    clientGameController.useItem(entity1);
    clientGameController.useItem(entity1);

    TimeUnit.SECONDS.sleep(1);

    verify(serverItemActionService, times(2)).use(anyObject(), anyObject());
    verify(clientItemActionService, times(0)).use(anyObject(), anyObject());
  }

  @Test
  public void TestServerUsesDefaultItem()
      throws InterruptedException, ChunkNotFound, EntityNotFound {

    doNothing().when(serverItemActionService).use(anyObject(), anyObject());
    doNothing().when(clientItemActionService).use(anyObject(), anyObject());

    Entity entity1 = serverGameController.createEntity(new Coordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    serverGameController.useItem(entity1);
    serverGameController.useItem(entity1);

    TimeUnit.SECONDS.sleep(1);

    verify(serverItemActionService, times(2)).use(anyObject(), anyObject());
    verify(clientItemActionService, times(0)).use(anyObject(), anyObject());
  }
}

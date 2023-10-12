package core.entity.attributes.inventory;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;
import core.app.game.Game;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.CommonFactory;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.BaseServerConfig;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.attributes.inventory.item.comsumers.ItemActionService;
import core.entity.controllers.factories.EntityControllerFactory;
import core.mock.GdxTestRunner;
import core.networking.client.ClientNetworkHandle;
import core.networking.server.ServerNetworkHandle;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

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
      throws IOException,
          InterruptedException,
          SerializationDataMissing,
          WrongVersion,
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

    serverInjector.injectMembers(serverItemActionService);
    clientInjector.injectMembers(clientItemActionService);

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

    when(serverItemActionService.checkTriggerGCD(anyObject())).thenReturn(true);
    when(clientItemActionService.checkTriggerGCD(anyObject())).thenReturn(true);

    Entity entity1 = serverGameController.createEntity(CommonFactory.createCoordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    clientGameController.useItem(entity1);
    clientGameController.useItem(entity1);

    TimeUnit.SECONDS.sleep(1);

    verify(serverItemActionService, times(2)).use(anyObject(), anyObject());
    verify(clientItemActionService, times(0)).use(anyObject(), anyObject());
  }

  @Test
  public void TestClientGcdTimeout() throws ChunkNotFound, InterruptedException, EntityNotFound {

    doNothing().when(serverItemActionService).use(anyObject(), anyObject());
    doNothing().when(clientItemActionService).use(anyObject(), anyObject());

    Entity entity1 = serverGameController.createEntity(CommonFactory.createCoordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    clientGameController.useItem(entity1);
    clientGameController.useItem(entity1);

    TimeUnit.SECONDS.sleep(1);

    verify(serverItemActionService, times(1)).use(anyObject(), anyObject());
    verify(clientItemActionService, times(0)).use(anyObject(), anyObject());
  }

  @Test
  public void TestServerUsesDefaultItem()
      throws InterruptedException, ChunkNotFound, EntityNotFound {

    doNothing().when(serverItemActionService).use(anyObject(), anyObject());
    doNothing().when(clientItemActionService).use(anyObject(), anyObject());
    when(serverItemActionService.checkTriggerGCD(anyObject())).thenReturn(true);
    when(clientItemActionService.checkTriggerGCD(anyObject())).thenReturn(true);
    // when check  GCD return true

    Entity entity1 = serverGameController.createEntity(CommonFactory.createCoordinates(0, 0));

    TimeUnit.SECONDS.sleep(1);

    serverGameController.useItem(entity1);
    serverGameController.useItem(entity1);

    TimeUnit.SECONDS.sleep(1);

    verify(serverItemActionService, times(2)).use(anyObject(), anyObject());
    verify(clientItemActionService, times(0)).use(anyObject(), anyObject());
  }
}

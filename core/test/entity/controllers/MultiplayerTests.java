package entity.controllers;

import app.GameController;
import app.game.Game;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.Entity;
import entity.attributes.msc.Coordinates;
import entity.attributes.msc.Health;
import entity.controllers.factories.EntityControllerFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.mock.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class MultiplayerTests {

  Injector clientInjector;
  Injector serverInjector;

  ClientNetworkHandle clientNetworkHandle;
  ServerNetworkHandle serverNetworkHandle;

  GameStore serverGameStore;
  GameStore clientGameStore;

  GameController serverGameController;
  GameController clientGameController;

  Game clientGame, serverGame;

  EntityControllerFactory clientEntityControllerFactory;

  @Before
  public void setup()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
          BodyNotFound {
    // block implementation of handshake
    clientInjector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new BaseServerConfig());

    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);
    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);

    clientGame = clientInjector.getInstance(Game.class);
    serverGame = serverInjector.getInstance(Game.class);

    clientGameController = clientInjector.getInstance(GameController.class);
    serverGameController = serverInjector.getInstance(GameController.class);

    serverGameStore = serverInjector.getInstance(GameStore.class);
    clientGameStore = clientInjector.getInstance(GameStore.class);

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
  public void testClientUpdateHealthNoKill()
      throws ChunkNotFound, InterruptedException, EntityNotFound {
    Entity client_entity = clientGameController.createEntity(new Coordinates(0, 0));
    client_entity.setEntityController(
        clientEntityControllerFactory.createEntityUserController(client_entity));

    TimeUnit.SECONDS.sleep(1);

    clientGameStore.getEntity(client_entity.getUuid());
    serverGameStore.getEntity(client_entity.getUuid());

    assert clientGameStore
        .getEntity(client_entity.getUuid())
        .equals(serverGameStore.getEntity(client_entity.getUuid()));

    Health newHealth = new Health(-100);

    clientGameController.updateEntityAttribute(client_entity.getUuid(), newHealth);

    TimeUnit.SECONDS.sleep(1);

    assert clientGameStore.getEntity(client_entity.getUuid()).getHealth().equals(newHealth);
    assert serverGameStore.getEntity(client_entity.getUuid()).getHealth().equals(newHealth);
  }

  @Test
  public void testServerUpdateHealthKill()
      throws ChunkNotFound, InterruptedException, EntityNotFound {
    Entity client_entity = clientGameController.createEntity(new Coordinates(0, 0));
    client_entity.setEntityController(
        clientEntityControllerFactory.createEntityUserController(client_entity));

    TimeUnit.SECONDS.sleep(1);

    clientGameStore.getEntity(client_entity.getUuid());
    serverGameStore.getEntity(client_entity.getUuid());

    assert clientGameStore
        .getEntity(client_entity.getUuid())
        .equals(serverGameStore.getEntity(client_entity.getUuid()));

    Health newHealth = new Health(-100);

    serverGameController.updateEntityAttribute(client_entity.getUuid(), newHealth);

    TimeUnit.SECONDS.sleep(1);

    assert !clientGameStore.doesEntityExist(client_entity.getUuid());
    assert !serverGameStore.doesEntityExist(client_entity.getUuid());
  }
}

package networking.connected;

import app.game.Game;
import app.game.GameController;
import app.screen.BaseCamera;
import app.screen.assets.animations.AnimationState;
import app.user.User;
import chunk.ActiveChunkManager;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Clock;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.BaseServerConfig;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.inventory.item.EmptyInventoryItem;
import entity.attributes.inventory.item.OrbInventoryItem;
import entity.attributes.msc.AnimationStateWrapper;
import entity.attributes.msc.Coordinates;
import entity.attributes.msc.Health;
import generation.ChunkGenerationService;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import networking.sync.SyncService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.mock.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class testClientServerAttributes {

  Injector clientInjector;
  Injector serverInjector;
  ClientNetworkHandle clientNetworkHandle;
  ServerNetworkHandle serverNetworkHandle;

  Game serverGame;
  Game clientGame;

  GameStore serverGameStore;
  GameStore clientGameStore;

  ChunkFactory chunkFactory;
  EntityFactory entityFactory;

  GameController clientGameController;
  GameController serverGameController;

  User serverUser;
  User clientUser;

  Clock serverClock;
  Clock clientClock;

  SyncService serverSyncService;
  SyncService clientSyncService;

  ActiveChunkManager serverActiveChunkManager;

  ChunkGenerationService serverChunkGenerationService;

  BaseCamera clientCamera;

  @Before
  public void setup() throws Exception {
    clientInjector = Guice.createInjector(new ClientConfig());
    serverInjector = Guice.createInjector(new BaseServerConfig());

    clientNetworkHandle = clientInjector.getInstance(ClientNetworkHandle.class);
    serverNetworkHandle = serverInjector.getInstance(ServerNetworkHandle.class);

    chunkFactory = serverInjector.getInstance(ChunkFactory.class);
    entityFactory = serverInjector.getInstance(EntityFactory.class);

    serverGameStore = serverInjector.getInstance(GameStore.class);
    clientGameStore = clientInjector.getInstance(GameStore.class);

    serverGameController = serverInjector.getInstance(GameController.class);
    clientGameController = clientInjector.getInstance(GameController.class);

    serverGame = serverInjector.getInstance(Game.class);
    clientGame = clientInjector.getInstance(Game.class);

    serverUser = serverInjector.getInstance(User.class);
    clientUser = clientInjector.getInstance(User.class);

    serverClock = serverInjector.getInstance(Clock.class);
    clientClock = clientInjector.getInstance(Clock.class);

    serverSyncService = serverInjector.getInstance(SyncService.class);
    clientSyncService = clientInjector.getInstance(SyncService.class);

    serverChunkGenerationService = serverInjector.getInstance(ChunkGenerationService.class);

    serverActiveChunkManager = serverInjector.getInstance(ActiveChunkManager.class);

    clientCamera = clientInjector.getInstance(BaseCamera.class);

    serverGame.start();
    //    clientGame.start();

    serverChunkGenerationService.blockedChunkRangeToGenerate(new ChunkRange(new Coordinates(0, 0)));
    TimeUnit.SECONDS.sleep(1);
  }

  @After
  public void cleanup() {
    try {
      clientNetworkHandle.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      serverNetworkHandle.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCreateHealth()
      throws WrongVersion, SerializationDataMissing, IOException, InterruptedException,
          BodyNotFound, ChunkNotFound, EntityNotFound {
    // connect
    // create entity, make sure equal

    clientGame.start();
    TimeUnit.SECONDS.sleep(1);
    Entity serverEntity = serverGameController.createEntity(new Coordinates(1, 1));
    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);
  }

  @Test
  public void testCreateHealthNotDefault()
      throws ChunkNotFound, WrongVersion, SerializationDataMissing, IOException,
          InterruptedException, BodyNotFound, EntityNotFound {
    // create entity, health is not default

    Entity serverEntity = serverGameController.createEntity(new Coordinates(1, 1));
    serverEntity.health = new Health(50);

    TimeUnit.SECONDS.sleep(1);
    clientGame.start();

    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);
  }

  @Test
  public void testUpdateHealth()
      throws ChunkNotFound, InterruptedException, EntityNotFound, WrongVersion,
          SerializationDataMissing, IOException, BodyNotFound {
    // update health

    Entity serverEntity = serverGameController.createEntity(new Coordinates(1, 1));
    Health h1 = new Health(50);

    serverGameController.updateEntityAttribute(serverEntity.getUuid(), h1);

    serverClock.waitForTick();

    assert serverEntity.health.equals(h1);

    clientGame.start();

    serverActiveChunkManager.addUserChunkSubscriptions(
        clientUser.getUserID(), new ChunkRange(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);
    assert clientGameStore.getEntity(serverEntity.getUuid()).health.equals(h1);

    Health h2 = new Health(75);

    serverGameController.updateEntityAttribute(serverEntity.getUuid(), h2);

    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);
    assert clientGameStore.getEntity(serverEntity.getUuid()).health.equals(h2);
  }

  @Test
  public void testInventoryUpdate()
      throws ChunkNotFound, InterruptedException, EntityNotFound, WrongVersion,
          SerializationDataMissing, IOException, BodyNotFound {
    // test inventory is no default

    Entity serverEntity = serverGameController.createEntity(new Coordinates(1, 1));
    OrbInventoryItem orb1 = new OrbInventoryItem(2);

    serverGameController.updateEntityAttribute(serverEntity.getUuid(), orb1);

    serverClock.waitForTick();

    clientGame.start();

    serverActiveChunkManager.addUserChunkSubscriptions(
        clientUser.getUserID(), new ChunkRange(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);
    Entity clientEntity = clientGameStore.getEntity(serverEntity.getUuid());

    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);
    assert clientEntity.getBag().getItem(3) instanceof EmptyInventoryItem;

    OrbInventoryItem orb2 = new OrbInventoryItem(3);

    serverGameController.updateEntityAttribute(serverEntity.getUuid(), orb2);

    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);

    assert clientEntity.getBag().getItem(3) instanceof OrbInventoryItem;
  }

  @Test
  public void testAnimationStateUpdate()
      throws ChunkNotFound, InterruptedException, EntityNotFound, WrongVersion,
          SerializationDataMissing, IOException, BodyNotFound {
    // test inventory is no default

    Entity serverEntity = serverGameController.createEntity(new Coordinates(1, 1));
    AnimationStateWrapper animationStateWrapper =
        new AnimationStateWrapper(AnimationState.ATTACKING);

    serverGameController.updateEntityAttribute(serverEntity.getUuid(), animationStateWrapper);

    serverClock.waitForTick();

    clientGame.start();

    serverActiveChunkManager.addUserChunkSubscriptions(
        clientUser.getUserID(), new ChunkRange(new Coordinates(0, 0)));

    TimeUnit.SECONDS.sleep(1);
    Entity clientEntity = clientGameStore.getEntity(serverEntity.getUuid());

    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);
    assert clientEntity
        .getAnimationStateWrapper()
        .getAnimationState()
        .equals(AnimationState.ATTACKING);

    AnimationStateWrapper animationStateWrapper2 =
        new AnimationStateWrapper(AnimationState.FALLING);

    serverGameController.updateEntityAttribute(serverEntity.getUuid(), animationStateWrapper2);

    TimeUnit.SECONDS.sleep(1);
    assert clientGameStore.getEntity(serverEntity.getUuid()).equals(serverEntity);

    assert clientEntity
        .getAnimationStateWrapper()
        .getAnimationState()
        .equals(AnimationState.FALLING);
  }
}

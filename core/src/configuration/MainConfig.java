package configuration;

import app.screen.BaseAssetManager;
import app.screen.BaseCamera;
import app.user.User;
import chunk.ActiveChunkManager;
import chunk.ChunkFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import common.ChunkClockMap;
import common.Clock;
import common.GameSettings;
import common.GameStore;
import common.events.EventService;
import entity.ActiveEntityManager;
import entity.EntityFactory;
import entity.block.BlockFactory;
import entity.collision.CollisionService;
import entity.collision.EntityContactListenerFactory;
import entity.collision.RayCastService;
import entity.collision.ladder.EntityLadderContact;
import entity.collision.left.EntityLeftContact;
import entity.collision.right.EntityRightContact;
import entity.collision.right.ground.EntityGroundContact;
import entity.controllers.EntityControllerFactory;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.EdgeStore;
import generation.BlockGenerator;
import generation.ChunkBuilderFactory;
import generation.ChunkGenerationService;
import networking.ConnectionStore;
import networking.ObserverFactory;
import networking.events.EventTypeFactory;
import networking.ping.PingService;
import networking.sync.SyncService;

public abstract class MainConfig extends AbstractModule {
  @Override
  protected void configure() {
    bind(BaseAssetManager.class).in(Singleton.class);
    bind(BaseCamera.class).in(Singleton.class);
    bind(Clock.class).asEagerSingleton();
    bind(GameStore.class).asEagerSingleton();
    bind(BlockGenerator.class).asEagerSingleton();
    bind(EventService.class).asEagerSingleton();
    bind(ConnectionStore.class).asEagerSingleton();

    bind(ChunkClockMap.class).asEagerSingleton();
    bind(ChunkFactory.class).asEagerSingleton();
    bind(EntityFactory.class).asEagerSingleton();
    bind(BlockFactory.class).asEagerSingleton();

    bind(ChunkBuilderFactory.class).asEagerSingleton();

    bind(EntityControllerFactory.class).asEagerSingleton();

    bind(EntityActionFactory.class).asEagerSingleton();
    bind(EntityContactListenerFactory.class).asEagerSingleton();
    bind(EntityGroundContact.class).asEagerSingleton();
    bind(EntityLeftContact.class).asEagerSingleton();
    bind(EntityRightContact.class).asEagerSingleton();
    bind(EdgeStore.class).asEagerSingleton();

    bind(EventTypeFactory.class).asEagerSingleton();
    bind(ObserverFactory.class).asEagerSingleton();
    bind(EntityLadderContact.class).asEagerSingleton();
    bind(CollisionService.class).asEagerSingleton();
    bind(User.class).asEagerSingleton();
    bind(ActiveEntityManager.class).asEagerSingleton();
    bind(ChunkGenerationService.class).asEagerSingleton();
    bind(ActiveChunkManager.class).asEagerSingleton();
    bind(User.class).in(Singleton.class);
    bind(GameSettings.class).in(Singleton.class);
    bind(PingService.class).asEagerSingleton();
    bind(SyncService.class).asEagerSingleton();
    bind(RayCastService.class).asEagerSingleton();
  }
}

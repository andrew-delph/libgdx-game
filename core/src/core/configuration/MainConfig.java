package core.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import core.app.screen.BaseCamera;
import core.app.screen.assets.BaseAssetManager;
import core.app.screen.assets.animations.AnimationManager;
import core.app.user.User;
import core.chunk.ActiveChunkManager;
import core.chunk.ChunkFactory;
import core.common.ChunkClockMap;
import core.common.Clock;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.ActiveEntityManager;
import core.entity.EntityFactory;
import core.entity.block.BlockFactory;
import core.entity.collision.CollisionService;
import core.entity.collision.EntityContactListenerFactory;
import core.entity.collision.RayCastService;
import core.entity.collision.ground.EntityGroundContact;
import core.entity.collision.ladder.EntityLadderContact;
import core.entity.collision.left.EntityLeftContact;
import core.entity.collision.orb.OrbContact;
import core.entity.collision.projectile.ProjectileContact;
import core.entity.collision.right.EntityRightContact;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.groups.GroupService;
import core.entity.pathfinding.EdgeStore;
import core.generation.BlockGenerator;
import core.generation.ChunkBuilderFactory;
import core.generation.ChunkGenerationService;
import core.networking.ConnectionStore;
import core.networking.ObserverFactory;
import core.networking.events.EventTypeFactory;
import core.networking.ping.PingService;
import core.networking.sync.SyncService;

public abstract class MainConfig extends AbstractModule {
  @Override
  protected void configure() {
    bind(BaseAssetManager.class).in(Singleton.class);
    bind(AnimationManager.class).asEagerSingleton();
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
    bind(ProjectileContact.class).asEagerSingleton();
    bind(OrbContact.class).asEagerSingleton();
    bind(GroupService.class).asEagerSingleton();
  }
}

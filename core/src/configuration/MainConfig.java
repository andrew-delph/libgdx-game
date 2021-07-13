package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkSubscriptionService;
import infra.common.ChunkClockMap;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.common.render.BaseAssetManager;
import infra.entity.EntityFactory;
import infra.entity.block.BlockFactory;
import infra.entity.collision.ground.EntityContactListenerFactory;
import infra.entity.collision.ground.EntityGroundContact;
import infra.entity.controllers.EntityControllerFactory;
import infra.entity.controllers.actions.EntityActionFactory;
import infra.entity.pathfinding.template.EdgeStore;
import infra.generation.BlockGenerator;
import infra.generation.ChunkBuilderFactory;
import infra.generation.ChunkGenerationManager;
import infra.networking.ConnectionStore;
import infra.networking.ObserverFactory;
import infra.networking.events.EventFactory;

public abstract class MainConfig extends AbstractModule {
  @Override
  protected void configure() {
    bind(BaseAssetManager.class).in(Singleton.class);
    bind(Clock.class).asEagerSingleton();
    bind(GameStore.class).asEagerSingleton();
    bind(ChunkGenerationManager.class).asEagerSingleton();
    bind(BlockGenerator.class).asEagerSingleton();
    bind(EventService.class).asEagerSingleton();
    bind(ConnectionStore.class).asEagerSingleton();
    bind(ChunkSubscriptionService.class).asEagerSingleton();

    bind(ChunkClockMap.class).asEagerSingleton();
    //
    bind(ChunkFactory.class).asEagerSingleton();
    //
    bind(EntityFactory.class).asEagerSingleton();
    bind(BlockFactory.class).asEagerSingleton();

    bind(ChunkBuilderFactory.class).asEagerSingleton();

    bind(EntityControllerFactory.class).asEagerSingleton();

    bind(EntityActionFactory.class).asEagerSingleton();
    bind(EntityContactListenerFactory.class).asEagerSingleton();
    bind(EntityGroundContact.class).asEagerSingleton();
    bind(EdgeStore.class).asEagerSingleton();

    //
    bind(EventFactory.class).asEagerSingleton();
    //
    bind(ObserverFactory.class).asEagerSingleton();
  }
}

package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkSubscriptionService;
import infra.common.ChunkClockMap;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.common.render.BaseAssetManager;
import infra.entity.EntityBodyBuilder;
import infra.entity.EntityFactory;
import infra.entity.block.BlockFactory;
import infra.entity.collision.CollisionService;
import infra.entity.collision.EntityContactListenerFactory;
import infra.entity.collision.contact.EntityGroundContact;
import infra.entity.controllers.EntityControllerFactory;
import infra.entity.controllers.actions.EntityActionFactory;
import infra.entity.pathfinding.template.BlockStructureFactory;
import infra.entity.pathfinding.Graph;
import infra.entity.pathfinding.PathFactory;
import infra.entity.pathfinding.VertexFactory;
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
    bind(Graph.class).asEagerSingleton();
    bind(EntityGroundContact.class).asEagerSingleton();

    bind(CollisionService.class).asEagerSingleton();

    bind(BlockStructureFactory.class).asEagerSingleton();

    bind(EntityBodyBuilder.class).asEagerSingleton();

    install(new FactoryModuleBuilder().build(ChunkFactory.class));

    install(new FactoryModuleBuilder().build(EntityFactory.class));

    install(new FactoryModuleBuilder().build(ChunkBuilderFactory.class));

    install(new FactoryModuleBuilder().build(BlockFactory.class));
    install(new FactoryModuleBuilder().build(EntityControllerFactory.class));

    install(new FactoryModuleBuilder().build(EventFactory.class));

    install(new FactoryModuleBuilder().build(ObserverFactory.class));

    install(new FactoryModuleBuilder().build(EntityContactListenerFactory.class));

    install(new FactoryModuleBuilder().build(PathFactory.class));

    install(new FactoryModuleBuilder().build(VertexFactory.class));

    install(new FactoryModuleBuilder().build(EntityControllerFactory.class));

    install(new FactoryModuleBuilder().build(EntityActionFactory.class));
  }
}

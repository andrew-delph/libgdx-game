package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkSubscriptionService;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.common.render.BaseAssetManager;
import infra.entity.EntityFactory;
import infra.entity.block.BlockFactory;
import infra.entity.controllers.EntityControllerFactory;
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

    install(new FactoryModuleBuilder().build(ChunkFactory.class));

    install(new FactoryModuleBuilder().build(EntityFactory.class));

    install(new FactoryModuleBuilder().build(ChunkBuilderFactory.class));

    install(new FactoryModuleBuilder().build(BlockFactory.class));
    install(new FactoryModuleBuilder().build(EntityControllerFactory.class));

    install(new FactoryModuleBuilder().build(EventFactory.class));

    install(new FactoryModuleBuilder().build(ObserverFactory.class));
  }
}

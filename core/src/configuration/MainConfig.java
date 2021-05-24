package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import infra.app.Game;
import infra.chunk.ChunkFactory;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.render.BaseAssetManager;
import infra.entity.EntityFactory;
import infra.entity.block.BlockFactory;
import infra.generation.BlockGenerator;
import infra.generation.ChunkBuilderFactory;
import infra.generation.ChunkGenerationManager;

public class MainConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(BaseAssetManager.class).in(Singleton.class);
        bind(Clock.class).asEagerSingleton();
        bind(GameStore.class).asEagerSingleton();
        bind(Game.class).asEagerSingleton();
        bind(ChunkGenerationManager.class).asEagerSingleton();
        bind(BlockGenerator.class).asEagerSingleton();
        install(new FactoryModuleBuilder()
                .build(ChunkFactory.class));

        install(new FactoryModuleBuilder()
                .build(EntityFactory.class));

        install(new FactoryModuleBuilder()
                .build(ChunkBuilderFactory.class));

        install(new FactoryModuleBuilder()
                .build(BlockFactory.class));
    }
}

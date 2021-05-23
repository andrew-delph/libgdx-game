package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import infra.chunk.ChunkFactory;
import infra.common.Clock;
import infra.common.GameStore;
import infra.entity.EntityFactory;
import infra.generation.ChunkBuilderFactory;

public class MainConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(Clock.class).asEagerSingleton();
        bind(GameStore.class).asEagerSingleton();
        install(new FactoryModuleBuilder()
                .build(ChunkFactory.class));

        install(new FactoryModuleBuilder()
                .build(EntityFactory.class));

        install(new FactoryModuleBuilder()
                .build(ChunkBuilderFactory.class));
    }
}

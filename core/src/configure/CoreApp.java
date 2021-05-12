package configure;

import com.google.inject.AbstractModule;
import game.User;
import generation.MapBuilder;
import generation.layer.fill.DirtFillLayer;
import generation.layer.random.StoneRandomLayer;
import infra.entity.EntityFactory;
import infra.entity.EntityManager;
import infra.events.EventService;
import infra.map.block.BlockFactory;
import networking.client.observers.ClientObserverFactory;
import networking.connection.ConnectionStore;
import networking.server.observers.ServerObserverFactory;
import render.MapRender;
import render.RenderManager;

public class CoreApp extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).asEagerSingleton();
        bind(EntityFactory.class).asEagerSingleton();
        bind(ConnectionStore.class).asEagerSingleton();
        bind(ClientObserverFactory.class).asEagerSingleton();
        bind(ServerObserverFactory.class).asEagerSingleton();
        bind(EventService.class).asEagerSingleton();
        bind(BlockFactory.class).asEagerSingleton();
        bind(MapRender.class).asEagerSingleton();
        bind(RenderManager.class).asEagerSingleton();
        bind(MapBuilder.class).asEagerSingleton();
        bind(StoneRandomLayer.class).asEagerSingleton();
        bind(DirtFillLayer.class).asEagerSingleton();
        bind(User.class).asEagerSingleton();
    }
}

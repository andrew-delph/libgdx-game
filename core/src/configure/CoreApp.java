package configure;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import game.GameClient;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import infra.events.EventService;
import infra.map.block.BlockFactory;
import networking.client.ClientNetworkHandle;
import networking.client.observers.ClientObserverFactory;
import networking.connetion.ConnectionStore;
import networking.server.ServerNetworkHandle;
import networking.server.observers.ServerObserverFactory;
import render.MapRender;
import render.RenderManager;

public class CoreApp extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).asEagerSingleton();
        bind(EntityFactory.class).asEagerSingleton();
        bind(ConnectionStore.class).asEagerSingleton();
        bind(ServerNetworkHandle.class).asEagerSingleton();
        bind(ClientNetworkHandle.class).asEagerSingleton();
        bind(ClientObserverFactory.class).asEagerSingleton();
        bind(ServerObserverFactory.class).asEagerSingleton();
        bind(GameClient.class).asEagerSingleton();
        bind(EventService.class).asEagerSingleton();
        bind(ClientObserverFactory.class).asEagerSingleton();
        bind(Integer.class).annotatedWith(Names.named("CoordinateScale")).toInstance(15);
        bind(BlockFactory.class).asEagerSingleton();
        bind(MapRender.class).asEagerSingleton();

        bind(RenderManager.class).asEagerSingleton();


    }
}

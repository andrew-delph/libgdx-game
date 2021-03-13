package modules;

import com.google.inject.AbstractModule;
import game.GameClient;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import infra.events.EventService;
import networking.client.observers.ClientObserverFactory;
import networking.connetion.ConnectionStore;
import networking.server.ServerNetworkHandle;
import networking.server.observers.ServerObserverFactory;

public class App extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).asEagerSingleton();
        bind(EntityFactory.class).asEagerSingleton();
        bind(ConnectionStore.class).asEagerSingleton();
        bind(ServerNetworkHandle.class).asEagerSingleton();
        bind(ClientObserverFactory.class).asEagerSingleton();
        bind(ServerObserverFactory.class).asEagerSingleton();
        bind(GameClient.class).asEagerSingleton();
        bind(EventService.class).asEagerSingleton();
    }
}

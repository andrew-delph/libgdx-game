package modules;

import com.google.inject.AbstractModule;
import game.GameClient;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import networking.client.observers.ClientObserverFactory;
import networking.server.ServerNetworkHandle;
import networking.server.observers.ServerObserverFactory;
import networking.connetion.ConnectionStore;

public class App  extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).asEagerSingleton();
        bind(EntityFactory.class).asEagerSingleton();
        bind(ConnectionStore.class).asEagerSingleton();
        bind(ServerNetworkHandle.class).asEagerSingleton();
        bind(ClientObserverFactory.class).asEagerSingleton();
        bind(ServerObserverFactory.class).asEagerSingleton();
        bind(GameClient.class).asEagerSingleton();
    }
}

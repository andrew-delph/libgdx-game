package modules;

import com.google.inject.AbstractModule;
import game.GameClient;
import infra.entity.EntityManager;
import networking.client.ClientObserverFactory;
import networking.server.ServerNetworkHandle;
import networking.server.ServerObserverFactory;
import networking.server.connetion.ConnectionStore;

public class App  extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).to(EntityManager.class);
        bind(ConnectionStore.class).to(ConnectionStore.class);
        bind(ServerNetworkHandle.class).to(ServerNetworkHandle.class);
        bind(ClientObserverFactory.class).to(ClientObserverFactory.class);
        bind(ServerObserverFactory.class).to(ServerObserverFactory.class);
        bind(GameClient.class).to(GameClient.class);
    }
}

package modules;

import com.google.inject.AbstractModule;
import infra.entity.EntityManager;
import networking.server.connetion.ConnectionStore;

public class App  extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).to(EntityManager.class);
        bind(ConnectionStore.class).to(ConnectionStore.class);
    }
}

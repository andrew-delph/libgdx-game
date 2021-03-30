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

public class TestApp extends CoreApp {

    @Override
    protected void configure() {
        super.configure();
        bind(Boolean.class).annotatedWith(Names.named("provideTexture")).toInstance(false);
        bind(Integer.class).annotatedWith(Names.named("CoordinateScale")).toInstance(15);
    }
}

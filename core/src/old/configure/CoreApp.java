package old.configure;

import com.google.inject.AbstractModule;
import old.game.User;
import old.generation.MapBuilder;
import old.generation.layer.fill.DirtFillLayer;
import old.generation.layer.random.StoneRandomLayer;
import old.infra.entity.EntityFactory;
import old.infra.entity.EntityManager;
import old.infra.events.EventService;
import old.infra.map.block.BlockFactory;
import old.networking.client.observers.ClientObserverFactory;
import old.networking.connection.ConnectionStore;
import old.networking.server.observers.ServerObserverFactory;
import old.render.MapRender;
import old.render.RenderManager;

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

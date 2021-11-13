package configuration;

import app.Game;
import app.server.ServerGame;
import common.events.EventConsumer;
import networking.events.consumer.server.ServerEventConsumer;
import networking.server.ServerNetworkHandle;

public class ServerConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(EventConsumer.class).to(ServerEventConsumer.class).asEagerSingleton();
    bind(Game.class).to(ServerGame.class).asEagerSingleton();
    bind(ServerNetworkHandle.class).asEagerSingleton();
  }
}

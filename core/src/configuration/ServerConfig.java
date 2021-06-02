package configuration;

import infra.app.Game;
import infra.app.server.ServerGame;
import infra.networking.consumer.NetworkConsumer;
import infra.networking.consumer.ServerEventConsumer;
import infra.networking.server.ServerNetworkHandle;

public class ServerConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(NetworkConsumer.class).to(ServerEventConsumer.class).asEagerSingleton();
    bind(Game.class).to(ServerGame.class).asEagerSingleton();
    bind(ServerNetworkHandle.class).asEagerSingleton();
  }
}

package configuration;

import infra.app.Game;
import infra.app.UpdateLoop;
import infra.app.client.ClientGame;
import infra.app.client.ClientUpdateLoop;
import infra.common.render.BaseCamera;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.consumer.ClientEventConsumer;
import infra.networking.consumer.NetworkConsumer;

public class ClientConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(NetworkConsumer.class).to(ClientEventConsumer.class).asEagerSingleton();
    bind(ClientNetworkHandle.class).asEagerSingleton();
    bind(UpdateLoop.class).to(ClientUpdateLoop.class).asEagerSingleton();
    bind(Game.class).to(ClientGame.class).asEagerSingleton();
    bind(BaseCamera.class).asEagerSingleton();
  }
}

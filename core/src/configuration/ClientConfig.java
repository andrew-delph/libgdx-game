package configuration;

import infra.networking.client.ClientNetworkHandle;
import infra.networking.consumer.ClientEventConsumer;
import infra.networking.consumer.NetworkConsumer;

public class ClientConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(NetworkConsumer.class).to(ClientEventConsumer.class).asEagerSingleton();
    bind(ClientNetworkHandle.class).asEagerSingleton();
  }
}

package configuration;

import infra.networking.consumer.ClientNetworkConsumer;
import infra.networking.consumer.NetworkConsumer;

public class ClientConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(NetworkConsumer.class).to(ClientNetworkConsumer.class).asEagerSingleton();

  }
}

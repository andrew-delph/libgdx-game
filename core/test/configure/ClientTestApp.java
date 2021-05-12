package configure;

import game.GameClient;
import networking.client.ClientNetworkHandle;
import networking.events.ClientEventRegister;
import networking.events.EventRegister;

public class ClientTestApp extends TestApp {
  @Override
  protected void configure() {
    super.configure();
    bind(ClientNetworkHandle.class).asEagerSingleton();
    bind(EventRegister.class).to(ClientEventRegister.class).asEagerSingleton();
    bind(GameClient.class).asEagerSingleton();
  }
}

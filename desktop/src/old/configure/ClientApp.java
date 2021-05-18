package old.configure;

import old.game.GameClient;
import old.networking.client.ClientNetworkHandle;
import old.networking.events.ClientEventRegister;
import old.networking.events.EventRegister;

public class ClientApp extends DesktopApp {
  @Override
  protected void configure() {
    super.configure();
    bind(ClientNetworkHandle.class).asEagerSingleton();
    bind(EventRegister.class).to(ClientEventRegister.class).asEagerSingleton();
    bind(GameClient.class).asEagerSingleton();
  }
}

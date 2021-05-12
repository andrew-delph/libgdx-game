package configure;

import networking.events.EventRegister;
import networking.events.ServerEventRegister;
import networking.server.ServerNetworkHandle;

public class ServerApp extends DesktopApp {
  @Override
  protected void configure() {
    super.configure();
    bind(ServerNetworkHandle.class).asEagerSingleton();
    bind(EventRegister.class).to(ServerEventRegister.class).asEagerSingleton();
  }
}

package old.configure;

import old.networking.events.EventRegister;
import old.networking.events.ServerEventRegister;
import old.networking.server.ServerNetworkHandle;

public class ServerApp extends DesktopApp {
  @Override
  protected void configure() {
    super.configure();
    bind(ServerNetworkHandle.class).asEagerSingleton();
    bind(EventRegister.class).to(ServerEventRegister.class).asEagerSingleton();
  }
}

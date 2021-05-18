package old.configure;

import old.networking.events.EventRegister;
import old.networking.events.ServerEventRegister;
import old.networking.server.ServerNetworkHandle;

public class ServerTestApp extends TestApp {
  @Override
  protected void configure() {
    super.configure();
    bind(ServerNetworkHandle.class).asEagerSingleton();
    bind(EventRegister.class).to(ServerEventRegister.class).asEagerSingleton();
  }
}

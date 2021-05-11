package configure;

import networking.events.EventRegister;
import networking.events.ServerEventRegister;
import networking.server.ServerNetworkHandle;

public class ServerTestApp extends TestApp {
  @Override
  protected void configure() {
    super.configure();
    bind(ServerNetworkHandle.class).asEagerSingleton();
    bind(EventRegister.class).to(ServerEventRegister.class).asEagerSingleton();
  }
}

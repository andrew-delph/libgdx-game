package core.configuration;

import core.entity.pathfinding.EdgeRegistration;
import core.entity.pathfinding.EdgeRegistrationBase;

public class MainBaseServerConfig extends BaseServerConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(EdgeRegistrationBase.class).to(EdgeRegistration.class).asEagerSingleton();
  }
}

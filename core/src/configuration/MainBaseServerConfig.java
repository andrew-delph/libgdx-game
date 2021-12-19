package configuration;

import entity.pathfinding.EdgeRegistration;
import entity.pathfinding.EdgeRegistrationBase;

public class MainBaseServerConfig extends BaseServerConfig {
    @Override
    protected void configure() {
        super.configure();
        bind(EdgeRegistrationBase.class).to(EdgeRegistration.class).asEagerSingleton();
    }
}

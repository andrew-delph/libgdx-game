package configuration;

import app.Game;
import app.UpdateLoop;
import app.standalone.StandAloneUpdateLoop;
import common.events.EventConsumer;
import common.events.SoloEventConsumer;

public class StandAloneConfig extends MainConfig {
    @Override
    protected void configure() {
        super.configure();
        bind(Game.class).asEagerSingleton();
        bind(EventConsumer.class).to(SoloEventConsumer.class).asEagerSingleton();
        bind(UpdateLoop.class).to(StandAloneUpdateLoop.class);
    }
}

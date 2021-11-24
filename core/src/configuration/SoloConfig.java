package configuration;

import app.Game;
import common.events.EventConsumer;
import common.events.SoloEventConsumer;

public class SoloConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(Game.class).asEagerSingleton();
    bind(EventConsumer.class).to(SoloEventConsumer.class).asEagerSingleton();
  }
}

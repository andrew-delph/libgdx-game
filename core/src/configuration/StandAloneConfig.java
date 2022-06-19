package configuration;

import app.game.Game;
import app.update.StandAloneUpdateTask;
import app.update.UpdateTask;
import common.events.EventConsumer;
import common.events.SoloEventConsumer;
import entity.controllers.factories.BaseEntityControllerFactory;
import entity.controllers.factories.EntityControllerFactory;

public class StandAloneConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(Game.class).asEagerSingleton();
    bind(EventConsumer.class).to(SoloEventConsumer.class).asEagerSingleton();
    bind(UpdateTask.class).to(StandAloneUpdateTask.class);
    bind(EntityControllerFactory.class).to(BaseEntityControllerFactory.class).asEagerSingleton();
  }
}

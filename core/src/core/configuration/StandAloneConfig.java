package core.configuration;

import core.app.game.Game;
import core.app.update.StandAloneUpdateTask;
import core.app.update.UpdateTask;
import core.common.events.EventConsumer;
import core.common.events.SoloEventConsumer;
import core.entity.controllers.factories.BaseEntityControllerFactory;
import core.entity.controllers.factories.EntityControllerFactory;

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

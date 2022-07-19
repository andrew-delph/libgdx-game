package core.configuration;

import core.app.game.Game;
import core.app.game.ServerGame;
import core.app.update.ServerUpdateTask;
import core.app.update.UpdateTask;
import core.common.events.EventConsumer;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.controllers.factories.ServerEntityControllerFactory;
import core.networking.events.consumer.server.ServerEventConsumer;
import core.networking.server.ServerNetworkHandle;

public class BaseServerConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(EventConsumer.class).to(ServerEventConsumer.class).asEagerSingleton();
    bind(Game.class).to(ServerGame.class).asEagerSingleton();
    bind(ServerNetworkHandle.class).asEagerSingleton();
    bind(UpdateTask.class).to(ServerUpdateTask.class).asEagerSingleton();
    bind(EntityControllerFactory.class).to(ServerEntityControllerFactory.class).asEagerSingleton();
  }
}

package core.configuration;

import core.app.game.ClientGame;
import core.app.game.Game;
import core.app.screen.BaseCamera;
import core.app.screen.ClientGameScreen;
import core.app.screen.GameScreen;
import core.app.update.ClientUpdateTask;
import core.app.update.UpdateTask;
import com.google.inject.Singleton;
import core.common.events.EventConsumer;
import core.entity.controllers.factories.ClientEntityControllerFactory;
import core.entity.controllers.factories.EntityControllerFactory;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.consumer.client.ClientEventConsumer;

public class ClientConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(EventConsumer.class).to(ClientEventConsumer.class).asEagerSingleton();
    bind(ClientNetworkHandle.class).asEagerSingleton();
    bind(UpdateTask.class).to(ClientUpdateTask.class).asEagerSingleton();
    bind(Game.class).to(ClientGame.class).in(Singleton.class);
    bind(BaseCamera.class).asEagerSingleton();
    bind(GameScreen.class).to(ClientGameScreen.class).asEagerSingleton();
    bind(EntityControllerFactory.class).to(ClientEntityControllerFactory.class).asEagerSingleton();
  }
}

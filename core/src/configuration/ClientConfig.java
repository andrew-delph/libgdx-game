package configuration;

import app.game.ClientGame;
import app.game.Game;
import app.screen.BaseCamera;
import app.screen.ClientGameScreen;
import app.screen.GameScreen;
import app.update.ClientUpdateTask;
import app.update.UpdateTask;
import common.events.EventConsumer;
import networking.client.ClientNetworkHandle;
import networking.events.consumer.client.ClientEventConsumer;

public class ClientConfig extends MainConfig {
    @Override
    protected void configure() {
        super.configure();
        bind(EventConsumer.class).to(ClientEventConsumer.class).asEagerSingleton();
        bind(ClientNetworkHandle.class).asEagerSingleton();
        bind(UpdateTask.class).to(ClientUpdateTask.class).asEagerSingleton();
        bind(Game.class).to(ClientGame.class).asEagerSingleton();
        bind(BaseCamera.class).asEagerSingleton();
        bind(GameScreen.class).to(ClientGameScreen.class).asEagerSingleton();
    }
}

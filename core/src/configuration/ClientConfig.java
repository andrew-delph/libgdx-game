package configuration;

import app.Game;
import app.GameScreen;
import app.UpdateLoop;
import app.client.ClientGame;
import app.client.ClientGameScreen;
import app.client.ClientUpdateLoop;
import app.render.BaseCamera;
import common.events.EventConsumer;
import networking.client.ClientNetworkHandle;
import networking.events.consumer.client.ClientEventConsumer;

public class ClientConfig extends MainConfig {
    @Override
    protected void configure() {
        super.configure();
        bind(EventConsumer.class).to(ClientEventConsumer.class).asEagerSingleton();
        bind(ClientNetworkHandle.class).asEagerSingleton();
        bind(UpdateLoop.class).to(ClientUpdateLoop.class).asEagerSingleton();
        bind(Game.class).to(ClientGame.class).asEagerSingleton();
        bind(BaseCamera.class).asEagerSingleton();
        bind(GameScreen.class).to(ClientGameScreen.class).asEagerSingleton();
    }
}

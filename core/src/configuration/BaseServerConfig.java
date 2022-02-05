package configuration;

import app.game.Game;
import app.game.ServerGame;
import app.update.ServerUpdateTask;
import app.update.UpdateTask;
import common.events.EventConsumer;
import networking.events.consumer.server.ServerEventConsumer;
import networking.server.ServerNetworkHandle;

public class BaseServerConfig extends MainConfig {
    @Override
    protected void configure() {
        super.configure();
        bind(EventConsumer.class).to(ServerEventConsumer.class).asEagerSingleton();
        bind(Game.class).to(ServerGame.class).asEagerSingleton();
        bind(ServerNetworkHandle.class).asEagerSingleton();
        bind(UpdateTask.class).to(ServerUpdateTask.class).asEagerSingleton();
    }
}

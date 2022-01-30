package app.game;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import entity.pathfinding.EdgeRegistrationBase;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class ServerGame extends Game {

    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Inject
    EdgeRegistrationBase edgeRegistration;

    @Inject
    public ServerGame() throws Exception {
        super();
    }

    public static void main(String[] args) throws InterruptedException, IOException, SerializationDataMissing {
        Injector injector = Guice.createInjector(new BaseServerConfig());
        Game game = injector.getInstance(Game.class);
        game.start();

        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    @Override
    public void start() throws IOException, InterruptedException, SerializationDataMissing {
        edgeRegistration.edgeRegistration();
        super.start();
        serverNetworkHandle.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.serverNetworkHandle.close();
    }
}

package app.server;

import app.Game;
import chunk.ChunkFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import common.GameStore;
import common.events.EventConsumer;
import common.exceptions.SerializationDataMissing;
import configuration.BaseServerConfig;
import entity.collision.CollisionService;
import entity.pathfinding.EdgeRegistrationBase;
import generation.ChunkGenerationManager;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class ServerGame extends Game {

    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Inject
    EdgeRegistrationBase edgeRegistration;

    @Inject
    public ServerGame(
            GameStore gameStore,
            ChunkFactory chunkFactory,
            ChunkGenerationManager chunkGenerationManager,
            EventConsumer eventConsumer,
            CollisionService collisionService)
            throws Exception {
        super(gameStore, chunkFactory, chunkGenerationManager, eventConsumer, collisionService);
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

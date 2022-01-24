package app.game;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameStore;
import common.events.EventConsumer;
import common.exceptions.SerializationDataMissing;
import entity.collision.CollisionService;
import networking.client.ClientNetworkHandle;

import java.io.IOException;

public class ClientGame extends Game {

    @Inject
    ClientNetworkHandle clientNetworkHandle;

    @Inject
    public ClientGame() throws Exception {
        super();
    }

    @Override
    public void stop() {
        super.stop();
        this.clientNetworkHandle.close();
    }

    @Override
    public void start() throws IOException, InterruptedException, SerializationDataMissing {
        this.clientNetworkHandle.connect();
        super.start();
    }

    @Override
    public void init() throws SerializationDataMissing {
        this.gameStore.addChunk(this.clientNetworkHandle.requestChunkBlocking(new ChunkRange(new Coordinates(0, 0))));
    }
}

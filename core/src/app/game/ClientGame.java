package app.game;

import app.screen.BaseCamera;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.exceptions.SerializationDataMissing;
import networking.client.ClientNetworkHandle;

import java.io.IOException;

public class ClientGame extends Game {

    @Inject
    ClientNetworkHandle clientNetworkHandle;

    @Inject
    BaseCamera baseCamera;

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
        super.start();
    }

    @Override
    public void preStartInit() throws SerializationDataMissing {
//        super.preStartInit();
    }

    @Override
    public void postStartInit() throws SerializationDataMissing, InterruptedException {
        this.clientNetworkHandle.connect();

        for (ChunkRange chunkRange : baseCamera.getChunkRangeOnScreen()) {
            this.clientNetworkHandle.requestChunkBlocking(chunkRange);
        }
    }
}

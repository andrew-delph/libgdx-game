package app.game;

import app.screen.BaseCamera;
import chunk.ChunkRange;
import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Inject;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import java.io.IOException;
import networking.client.ClientNetworkHandle;

public class ClientGame extends Game {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Inject BaseCamera baseCamera;

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
  public void start()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
          BodyNotFound {
    super.start();
  }

  @Override
  public void preStartInit() throws SerializationDataMissing {
    //        super.preStartInit();
  }

  @Override
  public void postStartInit() throws SerializationDataMissing, InterruptedException, WrongVersion {
    this.clientNetworkHandle.connect();

    for (ChunkRange chunkRange : baseCamera.getChunkRangeOnScreen()) {
      this.clientNetworkHandle.requestChunkBlocking(chunkRange);
    }
  }
}

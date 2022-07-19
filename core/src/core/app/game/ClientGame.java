package core.app.game;

import com.google.inject.Inject;
import core.app.screen.BaseCamera;
import core.chunk.ChunkRange;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.networking.client.ClientNetworkHandle;
import java.io.IOException;

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

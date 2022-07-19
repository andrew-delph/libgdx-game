package core.app.game;

import core.chunk.world.exceptions.BodyNotFound;
import com.google.inject.Inject;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.entity.pathfinding.EdgeRegistrationBase;
import java.io.IOException;
import core.networking.server.ServerNetworkHandle;

public class ServerGame extends Game {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Inject EdgeRegistrationBase edgeRegistration;

  @Inject
  public ServerGame() throws Exception {
    super();
  }

  @Override
  public void start()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
      BodyNotFound {
    edgeRegistration.edgeRegistration();
    super.start();
  }

  @Override
  public void preStartInit() throws SerializationDataMissing {
    //        super.preStartInit();
  }

  @Override
  public void postStartInit()
      throws SerializationDataMissing, InterruptedException, IOException, WrongVersion {
    super.postStartInit();
    serverNetworkHandle.start();
  }

  @Override
  public void stop() {
    super.stop();
    this.serverNetworkHandle.close();
  }
}

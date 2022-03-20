package app.game;

import com.google.inject.Inject;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import entity.pathfinding.EdgeRegistrationBase;
import java.io.IOException;
import networking.server.ServerNetworkHandle;

public class ServerGame extends Game {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Inject EdgeRegistrationBase edgeRegistration;

  @Inject
  public ServerGame() throws Exception {
    super();
  }

  @Override
  public void start()
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion {
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

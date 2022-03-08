package app.game;

import com.google.inject.Inject;
import common.exceptions.SerializationDataMissing;
import entity.pathfinding.EdgeRegistrationBase;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class ServerGame extends Game {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Inject EdgeRegistrationBase edgeRegistration;

  @Inject
  public ServerGame() throws Exception {
    super();
  }

  @Override
  public void start() throws IOException, InterruptedException, SerializationDataMissing {
    edgeRegistration.edgeRegistration();
    super.start();
  }

  @Override
  public void preStartInit() throws SerializationDataMissing {
    //        super.preStartInit();
  }

  @Override
  public void postStartInit() throws SerializationDataMissing, InterruptedException, IOException {
    super.postStartInit();
    serverNetworkHandle.start();
  }

  @Override
  public void stop() {
    super.stop();
    this.serverNetworkHandle.close();
  }
}

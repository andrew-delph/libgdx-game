package core.app.screen;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.update.ClientUpdateTask;
import core.app.update.UpdateTask;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.networking.client.ClientNetworkHandle;

public class ClientGameScreen extends GameScreen {

  @Inject ClientNetworkHandle clientNetworkHandle;
  @Inject UpdateTask clientUpdateTask;

  @Inject
  public ClientGameScreen() {}

  @Override
  public void create() {
    super.create();
  }

  @Override
  protected void createMyEntity() {
    Coordinates requestCoordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange requestChunkRange = CommonFactory.createChunkRange(requestCoordinates);
    ((ClientUpdateTask) clientUpdateTask).addChunkReserve(requestChunkRange);

    try {
      myEntity = clientNetworkHandle.getEntity(requestCoordinates);
    } catch (SerializationDataMissing | ChunkNotFound e) {
      e.printStackTrace();
      return;
    }

    try {
      myEntity = gameStore.getEntity(myEntity.getUuid());
    } catch (EntityNotFound e) {
      e.printStackTrace();
    }
    Gdx.app.log(GameSettings.LOG_TAG,"my entity " + myEntity.getUuid());
    myEntity.setEntityController(entityControllerFactory.createEntityUserController(myEntity));
    myEntity.setEntityStateMachine(entityStateMachineFactory.createEntityStateMachine(myEntity));

    ((ClientUpdateTask) clientUpdateTask).removeChunkReserve(requestChunkRange);
  }
}

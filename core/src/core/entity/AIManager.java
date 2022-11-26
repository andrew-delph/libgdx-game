package core.entity;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.game.GameController;
import core.app.user.UserID;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.attributes.msc.CoordinatesWrapper;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.groups.Group;
import core.entity.groups.GroupService;
import core.entity.statemachine.EntityStateMachineFactory;
import java.util.UUID;

public class AIManager {

  @Inject GameStore gameStore;
  @Inject EntityFactory entityFactory;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject GroupService groupService;
  @Inject EntityStateMachineFactory entityStateMachineFactory;

  public Entity requestCreateAI(UserID userID, Coordinates coordinates, UUID target)
      throws EntityNotFound, ChunkNotFound {

    if (groupService.getInGroup(Group.AI_GROUP).size() >= GameSettings.AI_LIMIT) {
      Gdx.app.log(GameSettings.LOG_TAG, "AI limit reached. Not creating another.");
      return null;
    }

    Entity aiEntity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));
    activeEntityManager.registerActiveEntity(userID, aiEntity.getUuid());

    Entity aiTarget = gameStore.getEntity(target);
    aiEntity.setCoordinatesWrapper(new CoordinatesWrapper(coordinates));
    gameController.addEntity(aiEntity);

    aiEntity.setEntityController(
        entityControllerFactory.createEntityPathController(aiEntity, aiTarget));
    groupService.registerEntityGroup(aiEntity.getUuid(), Group.AI_GROUP);

    aiEntity.setEntityStateMachine(entityStateMachineFactory.createEntityStateMachine(aiEntity));

    return aiEntity;
  }
}

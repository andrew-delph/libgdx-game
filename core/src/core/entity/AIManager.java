package core.entity;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.app.user.UserID;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.attributes.msc.Coordinates;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.groups.Group;
import core.entity.groups.GroupService;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AIManager {

  final Logger LOGGER = LogManager.getLogger();

  @Inject GameStore gameStore;
  @Inject EntityFactory entityFactory;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject GroupService groupService;

  public Entity requestCreateAI(UserID userID, Coordinates coordinates, UUID target)
      throws EntityNotFound, ChunkNotFound {

    if (groupService.getInGroup(Group.AI_GROUP).size() >= GameSettings.AI_LIMIT) {
      LOGGER.info("AI limit reached. Not creating another.");
      return null;
    }

    Entity aiEntity = entityFactory.createEntity(new Coordinates(0, 0));
    activeEntityManager.registerActiveEntity(userID, aiEntity.getUuid());

    Entity aiTarget = gameStore.getEntity(target);
    aiEntity.coordinates = coordinates;
    gameController.addEntity(aiEntity);

    aiEntity.setEntityController(
        entityControllerFactory.createEntityPathController(aiEntity, aiTarget));
    groupService.registerEntityGroup(aiEntity.getUuid(), Group.AI_GROUP);

    return aiEntity;
  }
}

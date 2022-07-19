package core.networking.events.consumer.server.incoming;

import core.app.game.GameController;
import core.app.user.User;
import com.google.inject.Inject;
import core.common.GameStore;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.attributes.msc.Coordinates;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.groups.Group;
import core.entity.groups.GroupService;
import core.entity.ActiveEntityManager;
import core.entity.Entity;
import core.entity.EntityFactory;
import java.util.function.Consumer;

public class CreateAIEntityConsumerServer implements Consumer<EventType> {

  @Inject EntityFactory entityFactory;
  @Inject GameController gameController;
  @Inject
  EntityControllerFactory entityControllerFactory;
  @Inject GameStore gameStore;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;
  @Inject
  GroupService groupService;

  @Override
  public void accept(EventType eventType) {
    try {
      CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;
      Entity aiEntity = entityFactory.createEntity(new Coordinates(0, 0));
      activeEntityManager.registerActiveEntity(user.getUserID(), aiEntity.getUuid());
      Entity aiTarget = gameStore.getEntity(realEvent.getTarget());
      aiEntity.coordinates = realEvent.getCoordinates();
      gameController.addEntity(aiEntity);
      aiEntity.setEntityController(
          entityControllerFactory.createEntityPathController(aiEntity, aiTarget));
      groupService.registerEntityGroup(aiEntity.getUuid(), Group.AI_GROUP);
    } catch (EntityNotFound | ChunkNotFound e) {
      e.printStackTrace();
    }
  }
}

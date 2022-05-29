package networking.events.consumer.server.incoming;

import app.GameController;
import app.user.User;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.CreateAIEntityEventType;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.ActiveEntityManager;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.Coordinates;
import entity.controllers.EntityControllerFactory;
import entity.groups.Group;
import entity.groups.GroupService;
import java.util.function.Consumer;

public class CreateAIEntityConsumerServer implements Consumer<EventType> {

  @Inject EntityFactory entityFactory;
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject GameStore gameStore;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;
  @Inject GroupService groupService;

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

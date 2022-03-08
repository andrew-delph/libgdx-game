package common.events;

import com.google.inject.Inject;
import common.events.types.CreateAIEntityEventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.controllers.EntityControllerFactory;

public class SoloEventConsumer extends EventConsumer {

  @Inject EntityControllerFactory entityControllerFactory;

  @Override
  public void init() {
    super.init();

    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type,
        eventType -> {
          try {
            CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;
            System.out.println("CREATE AI " + realEvent.getCoordinates());

            Entity aiEntity = gameController.createEntity(realEvent.getCoordinates());

            Entity aiTarget = gameStore.getEntity(realEvent.getTarget());

            aiEntity.setController(
                entityControllerFactory.createEntityPathController(aiEntity, aiTarget));

          } catch (EntityNotFound e) {
            e.printStackTrace();
          }
        });
  }
}

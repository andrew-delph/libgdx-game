package common.events;

import com.google.inject.Inject;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.EntityControllerFactory;

public class SoloEventConsumer extends EventConsumer {

  @Inject EntityFactory entityFactory;

  @Inject EntityControllerFactory entityControllerFactory;

  @Override
  public void init() {
    super.init();

    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type,
        eventType -> {
            System.out.println("hi");
          CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;

          Entity aiEntity = entityFactory.createEntity();

          entityFactory.createEntity();
          aiEntity.coordinates = new Coordinates(0, 1);
          gameController.createEntity(aiEntity);
          aiEntity.setController(
              entityControllerFactory.createEntityPathController(aiEntity, null));
        });
  }
}

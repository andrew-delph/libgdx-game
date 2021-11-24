package common.events;

import com.google.inject.Inject;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.EntityControllerFactory;
import generation.ChunkGenerationManager;

import java.util.List;
import java.util.Random;

public class SoloEventConsumer extends EventConsumer {

  @Inject EntityFactory entityFactory;

  @Inject EntityControllerFactory entityControllerFactory;

  @Inject ChunkGenerationManager chunkGenerationManager;

  @Override
  public void init() {
    super.init();

    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type,
        eventType -> {
          CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;

          Entity aiEntity = entityFactory.createEntity();

          aiEntity.coordinates = new Coordinates(0, 1);
          gameController.createEntity(aiEntity);

          // get random target
          List<Entity> activeEntityList = chunkGenerationManager.getActiveEntityList();
          Random rand = new Random();
          Entity randomTarget = activeEntityList.get(rand.nextInt(activeEntityList.size()));
          aiEntity.setController(
              entityControllerFactory.createEntityPathController(aiEntity, randomTarget));
        });
  }
}

package common.events;

import com.google.inject.Inject;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.EntityControllerFactory;
import generation.ChunkGenerationManager;

import java.util.List;
import java.util.Random;

public class SoloEventConsumer extends EventConsumer {

    @Inject
    EntityFactory entityFactory;

    @Inject
    EntityControllerFactory entityControllerFactory;

    @Inject
    ChunkGenerationManager chunkGenerationManager;

    @Override
    public void init() {
        super.init();

        this.eventService.addPostUpdateListener(
                CreateAIEntityEventType.type,
                eventType -> {
                    System.out.println("CREATE AI");
                    try {
                        CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;

                        Entity aiEntity = entityFactory.createEntity();

                        Entity aiTarget = gameStore.getEntity(realEvent.getTarget());

                        aiEntity.coordinates = realEvent.getCoordinates();
                        gameController.addEntity(aiEntity);

                        aiEntity.setController(
                                entityControllerFactory.createEntityPathController(aiEntity, aiTarget));

                    } catch (EntityNotFound e) {
                        e.printStackTrace();
                    }
                });
    }
}

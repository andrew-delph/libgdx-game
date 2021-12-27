package networking.events.consumer.server.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.events.types.CreateAIEntityEventType;
import common.events.types.EventType;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.EntityControllerFactory;
import generation.ChunkGenerationManager;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;


public class CreateAIEntityConsumerServer implements Consumer<EventType> {

    @Inject
    EntityFactory entityFactory;

    @Inject
    GameController gameController;

    @Inject
    ChunkGenerationManager chunkGenerationManager;

    @Inject
    EntityControllerFactory entityControllerFactory;

    @Override
    public void accept(EventType eventType) {
        System.out.println("CREATE AI");
        CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;

        Entity aiEntity = entityFactory.createEntity();

        aiEntity.coordinates = realEvent.getCoordinates();
        gameController.addEntity(aiEntity);

        // get random target
        List<Entity> activeEntityList = chunkGenerationManager.getActiveEntityList();
        Random rand = new Random();
        Entity randomTarget = activeEntityList.get(rand.nextInt(activeEntityList.size()));
        aiEntity.setController(
                entityControllerFactory.createEntityPathController(aiEntity, randomTarget));
    }
}

package networking.events.consumer.server.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.CreateAIEntityEventType;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.EntityControllerFactory;

import java.util.function.Consumer;


public class CreateAIEntityConsumerServer implements Consumer<EventType> {

    @Inject
    EntityFactory entityFactory;

    @Inject
    GameController gameController;

    @Inject
    EntityControllerFactory entityControllerFactory;

    @Inject
    GameStore gameStore;

    @Override
    public void accept(EventType eventType) {
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
    }
}

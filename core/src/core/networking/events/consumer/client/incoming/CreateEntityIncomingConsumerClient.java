package core.networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.GameStore;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.entity.Entity;
import core.entity.controllers.factories.EntityControllerFactory;
import core.networking.events.types.incoming.CreateEntityIncomingEventType;
import core.networking.translation.NetworkDataDeserializer;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateEntityIncomingConsumerClient implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject GameController gameController;
  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject GameStore gameStore;
  @Inject EntityControllerFactory entityControllerFactory;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) eventType;
    Entity entity;
    try {
      entity = entitySerializationConverter.createEntity(realEvent.getData());
      entity.setEntityController(entityControllerFactory.createRemoteBodyController(entity));
    } catch (SerializationDataMissing e) {
      LOGGER.error(e, e);
      // TODO disconnect the client
      return;
    }

    if (gameStore.doesEntityExist(entity.getUuid())) {
      LOGGER.debug("Entity already exists: " + entity.getUuid());
      return;
    }
    try {
      gameController.triggerAddEntity(entity);
    } catch (ChunkNotFound e) {
      LOGGER.error(e, e);
    }
  }
}

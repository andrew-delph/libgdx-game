package core.networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.entity.Entity;
import core.networking.events.types.incoming.CreateEntityIncomingEventType;
import core.networking.translation.NetworkDataDeserializer;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateEntityIncomingConsumerClient implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject GameController gameController;
  @Inject NetworkDataDeserializer entitySerializationConverter;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) eventType;
    Entity entity;
    try {
      entity = entitySerializationConverter.createEntity(realEvent.getData());
    } catch (SerializationDataMissing e) {
      LOGGER.error(e, e);
      // TODO disconnect the client
      return;
    }

    try {
      gameController.triggerAddEntity(entity);
    } catch (ChunkNotFound e) {
      LOGGER.error(e, e);
    }
  }
}
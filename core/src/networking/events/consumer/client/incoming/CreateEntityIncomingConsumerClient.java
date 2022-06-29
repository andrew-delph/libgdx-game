package networking.events.consumer.client.incoming;

import app.game.GameController;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import java.util.function.Consumer;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;
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

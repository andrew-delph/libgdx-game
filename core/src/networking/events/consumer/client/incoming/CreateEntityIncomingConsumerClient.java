package networking.events.consumer.client.incoming;

import app.GameController;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;

import java.util.function.Consumer;

public class CreateEntityIncomingConsumerClient implements Consumer<EventType> {

  @Inject GameController gameController;
  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) eventType;
    Entity entity;
    try {
      entity = entitySerializationConverter.createEntity(realEvent.getData());
    } catch (SerializationDataMissing e) {
      e.printStackTrace();
      // TODO disconnect the client
      return;
    }

    try {
      if (this.gameStore.getEntity(entity.uuid) != null) {
        return;
      }
    } catch (EntityNotFound e) {
      // pass
    }
    // TODO remove or update
    try {
      gameController.triggerAddEntity(entity);
    } catch (NullPointerException e) {
      System.out.println("e: " + new ChunkRange(entity.coordinates));
      throw e;
    }
  }
}

package networking.events.consumer.client.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventType;
import entity.Entity;
import entity.EntitySerializationConverter;
import networking.events.types.incoming.CreateEntityIncomingEventType;

import java.util.function.Consumer;

public class CreateEntityIncomingConsumerClient implements Consumer<EventType> {

  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;

  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) eventType;
    Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
    //           TODO remove or update
    if (this.gameStore.getEntity(entity.uuid) != null) {
      return;
    }
    gameController.triggerCreateEntity(entity);
  }
}

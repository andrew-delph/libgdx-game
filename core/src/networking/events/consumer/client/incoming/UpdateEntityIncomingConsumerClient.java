package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import entity.EntitySerializationConverter;
import networking.events.types.incoming.UpdateEntityIncomingEventType;

import java.util.function.Consumer;

public class UpdateEntityIncomingConsumerClient implements Consumer<EventType> {

  @Inject EntitySerializationConverter entitySerializationConverter;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
    entitySerializationConverter.updateEntity(realEvent.getData());
  }
}

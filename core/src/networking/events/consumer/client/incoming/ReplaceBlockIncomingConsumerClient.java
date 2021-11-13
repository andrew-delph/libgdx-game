package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.EventType;
import entity.EntitySerializationConverter;
import entity.block.Block;
import networking.events.EventFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;

import java.util.function.Consumer;

public class ReplaceBlockIncomingConsumerClient implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject EventFactory eventFactory;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockIncomingEventType realEvent = (ReplaceBlockIncomingEventType) eventType;
    this.eventService.queuePostUpdateEvent(
        this.eventFactory.createReplaceBlockEvent(
            realEvent.getTarget(),
            (Block)
                entitySerializationConverter.createEntity(realEvent.getReplacementBlockData())));
  }
}

package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;

public class ReplaceBlockOutgoingConsumerClient implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) eventType;
    this.eventService.queuePostUpdateEvent(
        EventTypeFactory.createReplaceEntityEvent(
            realEvent.getTarget(),
            realEvent.getReplacementEntity(),
            false,
            realEvent.getChunkRange()));
    this.clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}

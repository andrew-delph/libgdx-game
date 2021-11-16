package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import networking.client.ClientNetworkHandle;
import networking.events.EventFactory;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;

import java.util.function.Consumer;

public class ReplaceBlockOutgoingConsumerClient implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject ClientNetworkHandle clientNetworkHandle;
  @Inject EventFactory eventFactory;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) eventType;
    this.eventService.queuePostUpdateEvent(
        eventFactory.createReplaceBlockEvent(
            realEvent.getTarget(), realEvent.getReplacementBlock()));
    this.clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}

package networking.events.consumer.server.outgoing;

import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class ReplaceBlockOutgoingConsumerServer implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) eventType;
    this.eventService.queuePostUpdateEvent(eventType);
    for (UUID uuid : chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange())) {
      serverNetworkHandle.send(uuid, realEvent.toNetworkEvent());
    }
  }
}

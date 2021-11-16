package networking.events.consumer.server.outgoing;

import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class UpdateEntityOutgoingConsumerServer implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityOutgoingEventType realEvent = (UpdateEntityOutgoingEventType) eventType;
    NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
    for (UUID uuid : chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange())) {
      serverNetworkHandle.send(uuid, networkEvent);
    }
  }
}

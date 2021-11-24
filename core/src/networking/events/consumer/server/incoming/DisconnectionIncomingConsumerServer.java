package networking.events.consumer.server.incoming;

import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.types.EventType;
import entity.Entity;
import generation.ChunkGenerationManager;
import networking.ConnectionStore;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.DisconnectionIncomingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class DisconnectionIncomingConsumerServer implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject
  EventTypeFactory eventTypeFactory;
  @Inject ChunkGenerationManager chunkGenerationManager;
  @Inject ConnectionStore connectionStore;

  @Override
  public void accept(EventType eventType) {
    DisconnectionIncomingEventType realEvent = (DisconnectionIncomingEventType) eventType;
    connectionStore.removeConnection(realEvent.getUuid());
    for (UUID ownersEntityUuid : chunkGenerationManager.getOwnerUuidList(realEvent.getUuid())) {
      Entity entity = this.gameStore.getEntity(ownersEntityUuid);
      this.eventService.queuePostUpdateEvent(
          eventTypeFactory.createRemoveEntityEvent(ownersEntityUuid));

      RemoveEntityOutgoingEventType removeEntityOutgoingEvent =
          eventTypeFactory.createRemoveEntityOutgoingEvent(
              entity.toNetworkData(), new ChunkRange(entity.coordinates));
      for (UUID subscriptionUuid :
          chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
        serverNetworkHandle.send(subscriptionUuid, removeEntityOutgoingEvent.toNetworkEvent());
      }
    }
  }
}
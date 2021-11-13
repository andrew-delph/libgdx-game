package networking.events.consumer.server.incoming;

import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.EventType;
import entity.Entity;
import entity.EntitySerializationConverter;
import entity.block.Block;
import networking.events.EventFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class ReplaceBlockIncomingConsumerServer implements Consumer<EventType> {

  @Inject EventService eventService;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventFactory eventFactory;

  @Override
  public void accept(EventType eventType) {
    ReplaceBlockIncomingEventType realEvent = (ReplaceBlockIncomingEventType) eventType;
    Entity placedEntity = this.gameStore.getEntity(realEvent.getTarget());
    ChunkRange chunkRange = new ChunkRange(placedEntity.coordinates);
    this.eventService.queuePostUpdateEvent(
        this.eventFactory.createReplaceBlockEvent(
            realEvent.getTarget(),
            (Block)
                entitySerializationConverter.createEntity(realEvent.getReplacementBlockData())));
    for (UUID uuid : chunkSubscriptionService.getSubscriptions(chunkRange)) {
      serverNetworkHandle.send(uuid, realEvent.networkEvent);
    }
  }
}

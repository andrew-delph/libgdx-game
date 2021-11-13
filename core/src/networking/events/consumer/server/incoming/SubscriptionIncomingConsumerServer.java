package networking.events.consumer.server.incoming;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventType;
import entity.Entity;
import networking.events.EventFactory;
import networking.events.types.incoming.SubscriptionIncomingEventType;
import networking.server.ServerNetworkHandle;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SubscriptionIncomingConsumerServer implements Consumer<EventType> {
  ;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventFactory eventFactory;
  @Inject ChunkFactory chunkFactory;

  @Override
  public void accept(EventType eventType) {
    SubscriptionIncomingEventType realEvent = (SubscriptionIncomingEventType) eventType;
    List<ChunkRange> userChunkRangeList =
        chunkSubscriptionService.getUserChunkRangeSubscriptions(realEvent.getUser());
    Predicate<ChunkRange> doesNotContain = (userChunkRangeList::contains);
    doesNotContain = doesNotContain.negate();
    List<ChunkRange> newChunkRangeList =
        realEvent.getChunkRangeList().stream()
            .distinct()
            .filter(doesNotContain)
            .collect(Collectors.toList());
    chunkSubscriptionService.registerSubscription(
        realEvent.getUser(), realEvent.getChunkRangeList());
    for (ChunkRange chunkRange : newChunkRangeList) {
      Chunk chunk = gameStore.getChunk(chunkRange);
      if (chunk == null) {
        this.gameStore.addChunk(this.chunkFactory.create(chunkRange));
        chunk = gameStore.getChunk(chunkRange);
      }
      for (Entity entity : chunk.getEntityList()) {
        serverNetworkHandle.send(
            realEvent.getUser(),
            eventFactory
                .createCreateEntityOutgoingEvent(
                    entity.toNetworkData(), new ChunkRange(entity.coordinates))
                .toNetworkEvent());
      }
    }
  }
}

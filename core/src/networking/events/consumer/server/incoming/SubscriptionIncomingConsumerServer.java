package networking.events.consumer.server.incoming;

import chunk.ActiveChunkManager;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.types.EventType;
import generation.ChunkGenerationService;
import networking.events.types.incoming.SubscriptionIncomingEventType;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SubscriptionIncomingConsumerServer implements Consumer<EventType> {

  @Inject ActiveChunkManager activeChunkManager;
  @Inject ChunkGenerationService chunkGenerationService;

  @Override
  public void accept(EventType eventType) {
    SubscriptionIncomingEventType realEvent = (SubscriptionIncomingEventType) eventType;
    Set<ChunkRange> userChunkRangeList =
        activeChunkManager.getUserChunkRanges(realEvent.getUserID());
    Predicate<ChunkRange> doesNotContain = (userChunkRangeList::contains);
    doesNotContain = doesNotContain.negate();
    List<ChunkRange> newChunkRangeList =
        realEvent.getChunkRangeList().stream()
            .distinct()
            .filter(doesNotContain)
            .collect(Collectors.toList());

    activeChunkManager.setUserChunkSubscriptions(
        realEvent.getUserID(), realEvent.getChunkRangeList());

    chunkGenerationService.queueChunkRangeToGenerate(realEvent.getChunkRangeList());
  }
}

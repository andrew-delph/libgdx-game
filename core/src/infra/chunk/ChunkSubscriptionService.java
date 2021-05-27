package infra.chunk;

import com.google.inject.Inject;

import java.util.*;

public class ChunkSubscriptionService {

  Map<UUID, List<ChunkRange>> userToChunkList;
  Map<ChunkRange, List<UUID>> chunkRangeToUser;

  @Inject
  ChunkSubscriptionService() {
    this.userToChunkList = new HashMap<>();
    this.chunkRangeToUser = new HashMap<>();
  }

  public void registerSubscription(UUID uuid, List<ChunkRange> chunkRangeList) {
    this.userToChunkList.put(uuid, chunkRangeList);
    for (ChunkRange chunkRange : chunkRangeList) {
      this.chunkRangeToUser.computeIfAbsent(chunkRange, k -> new LinkedList<>());
      this.chunkRangeToUser.get(chunkRange).add(uuid);
    }
  }

  public List<UUID> getSubscriptions(ChunkRange chunkRange) {
    return this.chunkRangeToUser.get(chunkRange);
  }
}

package infra.chunk;

import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkSubscriptionService {

  Map<UUID, List<ChunkRange>> userToChunkList;
  Map<ChunkRange, List<UUID>> chunkRangeToUser;

  @Inject
  ChunkSubscriptionService() {
    this.userToChunkList = new ConcurrentHashMap<>();
    this.chunkRangeToUser = new ConcurrentHashMap<>();
  }

  public void registerSubscription(UUID uuid, List<ChunkRange> chunkRangeList) {
    this.userToChunkList.computeIfAbsent(uuid, k -> new LinkedList<>());
    chunkRangeList.addAll(this.userToChunkList.get(uuid));
    this.userToChunkList.put(uuid, chunkRangeList);
    for (ChunkRange chunkRange : chunkRangeList) {
      this.chunkRangeToUser.computeIfAbsent(chunkRange, k -> new LinkedList<>());
      this.chunkRangeToUser.get(chunkRange).add(uuid);
    }
  }

  public List<UUID> getSubscriptions(ChunkRange chunkRange) {
    this.chunkRangeToUser.computeIfAbsent(chunkRange, k -> new LinkedList<>());
    return this.chunkRangeToUser.get(chunkRange);
  }

  public List<ChunkRange> getUserChunkRangeSubscriptions(UUID uuid) {
    return this.userToChunkList.get(uuid);
  }
}

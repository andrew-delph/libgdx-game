package infra.chunk;

import com.google.inject.Inject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkSubscriptionService {

  Map<UUID, Set<ChunkRange>> userToChunkList;
  Map<ChunkRange, Set<UUID>> chunkRangeToUser;

  @Inject
  ChunkSubscriptionService() {
    this.userToChunkList = new ConcurrentHashMap<>();
    this.chunkRangeToUser = new ConcurrentHashMap<>();
  }

  public void registerSubscription(UUID uuid, List<ChunkRange> chunkRangeList) {
    this.userToChunkList.computeIfAbsent(uuid, k -> new HashSet<>());
    chunkRangeList.addAll(this.userToChunkList.get(uuid));
    this.userToChunkList.put(uuid, new HashSet<>(chunkRangeList));
    for (ChunkRange chunkRange : chunkRangeList) {
      this.chunkRangeToUser.computeIfAbsent(chunkRange, k -> new HashSet<>());
      this.chunkRangeToUser.get(chunkRange).add(uuid);
    }
  }

  public List<UUID> getSubscriptions(ChunkRange chunkRange) {
    this.chunkRangeToUser.computeIfAbsent(chunkRange, k -> new HashSet<>());
    return new LinkedList<>(this.chunkRangeToUser.get(chunkRange));
  }

  public void removeUUID(UUID uuid) {
    if(this.userToChunkList.get(uuid) == null){
      return;
    }
    for (ChunkRange chunkRange : this.userToChunkList.get(uuid)) {
      this.chunkRangeToUser.get(chunkRange).remove(uuid);
    }
  }

  public List<ChunkRange> getUserChunkRangeSubscriptions(UUID uuid) {
    return new LinkedList<>(this.userToChunkList.get(uuid));
  }
}

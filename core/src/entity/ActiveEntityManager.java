package entity;

import app.user.UserID;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;

import java.util.*;

public class ActiveEntityManager {

  private final Map<UserID, Set<UUID>> userIDEntityMap = new HashMap<>();
  @Inject GameStore gameStore;

  public void registerActiveEntity(UserID user_uuid, UUID entity_uuid) {
    userIDEntityMap.putIfAbsent(user_uuid, new HashSet<>());
    userIDEntityMap.get(user_uuid).add(entity_uuid);
  }

  public void deregisterUser(UserID user_uuid) {
    userIDEntityMap.remove(user_uuid);
  }

  public Set<UUID> getUserActiveEntitySet(UserID user_uuid) {
    return userIDEntityMap.getOrDefault(user_uuid, new HashSet<>());
  }

  public Set<UUID> getActiveEntities() {
    Set<UUID> allActiveEntity = new HashSet<>();
    for (Set<UUID> userSet : userIDEntityMap.values()) {
      allActiveEntity.addAll(userSet);
    }
    return allActiveEntity;
  }

  public Set<ChunkRange> getActiveChunkRanges() {
    Set<ChunkRange> allActiveChunkRange = new HashSet<>();
    for (UUID entityID : this.getActiveEntities()) {
      if (this.gameStore.getEntityChunkRange(entityID) != null) {
        allActiveChunkRange.add(this.gameStore.getEntityChunkRange(entityID));
      }
    }
    return allActiveChunkRange;
  }
}

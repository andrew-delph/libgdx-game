package core.entity;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.user.UserID;
import core.common.ChunkRange;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.exceptions.EntityNotFound;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;



public class ActiveEntityManager {


  private final Map<UserID, Set<UUID>> userIDEntityMap = new HashMap<>();
  @Inject GameStore gameStore;

  public void registerActiveEntity(UserID user_uuid, UUID entity_uuid) {
    userIDEntityMap.putIfAbsent(user_uuid, new HashSet<>());
    userIDEntityMap.get(user_uuid).add(entity_uuid);
  }

  public void removeActiveEntity(UserID user_uuid, UUID entity_uuid) {
    userIDEntityMap.putIfAbsent(user_uuid, new HashSet<>());
    userIDEntityMap.get(user_uuid).remove(entity_uuid);
  }

  public void removeActiveEntity(UUID entity_uuid) {
    Set<UUID> allActiveEntity = new HashSet<>();
    for (Set<UUID> userSet : userIDEntityMap.values()) {
      userSet.remove(entity_uuid);
    }
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
      ChunkRange temp;
      try {
        temp = this.gameStore.getEntityChunkRange(entityID);
      } catch (EntityNotFound e) {
        Gdx.app.error(GameSettings.LOG_TAG,e.getMessage(), e);
        continue;
      }
      allActiveChunkRange.add(temp);
    }
    return allActiveChunkRange;
  }
}

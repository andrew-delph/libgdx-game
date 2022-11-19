package core.entity.pathfinding;

import core.common.Coordinates;
import core.common.GameStore;
import core.entity.Entity;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityStructure {
  static int hashCount = 0;
  final int myHashCount;
  Map<RelativeCoordinates, Class<? extends Entity>> relativeEntityMap;
  GameStore gameStore;

  public EntityStructure(GameStore gameStore) {
    this(gameStore, new HashMap<>());
  }

  public EntityStructure(
      GameStore gameStore, Map<RelativeCoordinates, Class<? extends Entity>> relativeEntityMap) {
    this.gameStore = gameStore;
    this.relativeEntityMap = relativeEntityMap;
    myHashCount = hashCount++;
  }

  public void registerRelativeEntity(
      RelativeCoordinates relativeCoordinates, Class<? extends Entity> entityClass) {
    this.relativeEntityMap.put(relativeCoordinates, entityClass);
  }

  public Boolean verifyEntityStructure(
      PathGameStoreOverride pathGameStoreOverride, Coordinates coordinates) {

    for (Map.Entry<RelativeCoordinates, Class<? extends Entity>> entry :
        this.relativeEntityMap.entrySet()) {
      RelativeCoordinates currentRelativeCoordinates = entry.getKey();
      Class<? extends Entity> targetEntityType = entry.getValue();

      List<Class<? extends Entity>> existingEntityList =
          pathGameStoreOverride.getEntityListBaseCoordinates(
              currentRelativeCoordinates.applyRelativeCoordinates(coordinates));

      if (existingEntityList == null) {
        List<Entity> entityList =
            this.gameStore.getEntityListBaseCoordinates(
                currentRelativeCoordinates.applyRelativeCoordinates(coordinates));
        existingEntityList = new LinkedList<>();
        for (Entity e : entityList) {
          existingEntityList.add(e.getClass());
        }
      }

      boolean found = false;
      for (Class<? extends Entity> retrievedEntity : existingEntityList) {
        if (targetEntityType.isAssignableFrom(retrievedEntity)) {
          found = true;
          break;
        }
      }
      if (!found) return false;
    }
    return true;
  }

  public EntityStructure copy() {
    return new EntityStructure(this.gameStore, new HashMap<>(this.relativeEntityMap));
  }

  public Set<Map.Entry<RelativeCoordinates, Class<? extends Entity>>>
      getRelativeEntityMapEntrySet() {
    return this.relativeEntityMap.entrySet();
  }

  @Override
  public int hashCode() {
    return this.myHashCount;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    EntityStructure other = (EntityStructure) obj;
    return this.myHashCount == other.myHashCount;
  }
}

package infra.entity.pathfinding;

import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.entity.pathfinding.edge.AbstractEdge;

import java.util.*;

public class EntityStructure {
  Map<RelativeCoordinates, Class<? extends Entity>> relativeEntityMap;

  GameStore gameStore;
  static int hashCount = 0;
  final int myHashCount;

  public EntityStructure(GameStore gameStore) {
    this(gameStore,new HashMap<>());
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
      Class<? extends Entity> entityClass = entry.getValue();

      List<Class<? extends Entity>> classList =
          pathGameStoreOverride.getEntityListBaseCoordinates(
              currentRelativeCoordinates.applyRelativeCoordinates(coordinates));

      if (classList == null) {
        List<Entity> entityList =
            this.gameStore.getEntityListBaseCoordinates(
                currentRelativeCoordinates.applyRelativeCoordinates(coordinates));
        classList = new LinkedList<>();
        for (Entity e : entityList) {
          classList.add(e.getClass());
        }
      }

      boolean found = false;
      for (Class retrievedEntity : classList) {
        if (entityClass.isAssignableFrom(retrievedEntity)) {
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

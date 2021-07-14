package infra.entity.pathfinding.template;

import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityStructure {
  Map<RelativeCoordinates, Class<? extends Entity>> relativeEntityMap;

  GameStore gameStore;

  public EntityStructure(GameStore gameStore) {
    this.gameStore = gameStore;
    this.relativeEntityMap = new HashMap<>();
  }

  public EntityStructure(
      GameStore gameStore, Map<RelativeCoordinates, Class<? extends Entity>> relativeEntityMap) {
    this.gameStore = gameStore;
    this.relativeEntityMap = relativeEntityMap;
  }

  public void registerRelativeEntity(
      RelativeCoordinates relativeCoordinates, Class<? extends Entity> entityClass) {
    this.relativeEntityMap.put(relativeCoordinates, entityClass);
  }

  public Boolean verifyEntityStructure(Coordinates coordinates) {
    for (Map.Entry<RelativeCoordinates, Class<? extends Entity>> entry :
        this.relativeEntityMap.entrySet()) {
      RelativeCoordinates currentRelativeCoordinates = entry.getKey();
      Class<? extends Entity> entityClass = entry.getValue();

      List<Entity> entityList =
          this.gameStore.getEntityListBaseCoordinates(
              currentRelativeCoordinates.applyRelativeCoordinates(coordinates));

      boolean found = false;
      for (Entity retrievedEntity : entityList) {
        System.out.println("heeeeere" + retrievedEntity.getClass() + "  ,  " + entityClass);
        if (entityClass.isInstance(retrievedEntity)) {
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
}

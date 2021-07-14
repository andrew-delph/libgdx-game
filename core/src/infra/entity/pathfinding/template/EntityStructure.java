package infra.entity.pathfinding.template;

import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityStructure {
  Map<RelativeCoordinates, Class<? extends Entity>> relativeBlockMap;

  GameStore gameStore;

  public EntityStructure(GameStore gameStore) {
    this.gameStore = gameStore;
    this.relativeBlockMap = new HashMap<>();
  }

  public EntityStructure(
      GameStore gameStore, Map<RelativeCoordinates, Class<? extends Entity>> relativeBlockMap) {
    this.gameStore = gameStore;
    this.relativeBlockMap = relativeBlockMap;
  }

  public void registerRelativeBlock(
      RelativeCoordinates relativeCoordinates, Class<? extends Entity> blockClass) {
    this.relativeBlockMap.put(relativeCoordinates, blockClass);
  }

  public Boolean verifyBlockStructure(Coordinates coordinates) {
    for (Map.Entry<RelativeCoordinates, Class<? extends Entity>> entry :
        this.relativeBlockMap.entrySet()) {
      RelativeCoordinates currentRelativeCoordinates = entry.getKey();
      Class<? extends Entity> blockClass = entry.getValue();

      List<Entity> entityList =
          this.gameStore.getEntityListBaseCoordinates(
              currentRelativeCoordinates.applyRelativeCoordinates(coordinates));

      for (Entity retrievedEntity : entityList) {
        if (blockClass.isInstance(retrievedEntity)) {
          break;
        }
      }
      return false;
    }
    return true;
  }

  public EntityStructure copy() {
    return new EntityStructure(this.gameStore, new HashMap<>(this.relativeBlockMap));
  }

  public Set<Map.Entry<RelativeCoordinates, Class<? extends Entity>>>
      getRelativeBlockMapEntrySet() {
    return this.relativeBlockMap.entrySet();
  }
}

package infra.entity.pathfinding.template;

import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockStructure {
  Map<RelativeCoordinates, Class<? extends Block>> relativeBlockMap;

  GameStore gameStore;

  public BlockStructure(GameStore gameStore) {
    this.gameStore = gameStore;
    this.relativeBlockMap = new HashMap<>();
  }

  public BlockStructure(
      GameStore gameStore, Map<RelativeCoordinates, Class<? extends Block>> relativeBlockMap) {
    this.gameStore = gameStore;
    this.relativeBlockMap = relativeBlockMap;
  }

  public void registerRelativeBlock(
      RelativeCoordinates relativeCoordinates, Class<? extends Block> blockClass) {
    this.relativeBlockMap.put(relativeCoordinates, blockClass);
  }

  public Boolean verifyBlockStructure(Coordinates coordinates) {
    for (Map.Entry<RelativeCoordinates, Class<? extends Block>> entry :
        this.relativeBlockMap.entrySet()) {
      RelativeCoordinates currentRelativeCoordinates = entry.getKey();
      Class<? extends Block> blockClass = entry.getValue();
      Block retrievedBlock =
          this.gameStore.getBlock(currentRelativeCoordinates.applyRelativeCoordinates(coordinates));
      if (!blockClass.isInstance(retrievedBlock)) {
        return false;
      }
    }
    return true;
  }

  public BlockStructure copy() {
    return new BlockStructure(this.gameStore, new HashMap<>(this.relativeBlockMap));
  }

  public Set<Map.Entry<RelativeCoordinates, Class<? extends Block>>> getRelativeBlockMapEntrySet() {
    return this.relativeBlockMap.entrySet();
  }
}

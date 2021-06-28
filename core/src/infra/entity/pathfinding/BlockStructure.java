package infra.entity.pathfinding;

import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockStructure {
  Map<RelativeCoordinates, Class<? extends Block>> relativeBlockMap = new HashMap<>();

  GameStore gameStore;

  public BlockStructure(GameStore gameStore) {
    this.gameStore =gameStore;
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
      System.out.println(currentRelativeCoordinates.applyRelativeCoordinates(coordinates));
      Block retrievedBlock =
          this.gameStore.getBlock(currentRelativeCoordinates.applyRelativeCoordinates(coordinates));
      if (!blockClass.isInstance(retrievedBlock)) {
        return false;
      }
    }
    return true;
  }
}

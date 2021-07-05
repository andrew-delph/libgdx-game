package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.common.GameStore;

public class BlockStructureFactory {

  @Inject GameStore gameStore;

  public BlockStructure createBlockStructure() {
    return new BlockStructure(this.gameStore);
  }
}

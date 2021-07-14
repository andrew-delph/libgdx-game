package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.common.GameStore;

public class BlockStructureFactory {

  @Inject GameStore gameStore;

  public EntityStructure createBlockStructure() {
    return new EntityStructure(this.gameStore);
  }
}

package infra.entity.pathfinding;

import com.google.inject.Inject;
import infra.common.GameStore;

public class EntityStructureFactory {

  @Inject GameStore gameStore;

  public EntityStructure createEntityStructure() {
    return new EntityStructure(this.gameStore);
  }
}

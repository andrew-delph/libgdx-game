package entity.pathfinding;

import com.google.inject.Inject;
import common.GameStore;

public class EntityStructureFactory {

  @Inject GameStore gameStore;

  public EntityStructure createEntityStructure() {
    return new EntityStructure(this.gameStore);
  }
}

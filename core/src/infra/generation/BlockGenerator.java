package infra.generation;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.common.Coordinates;
import infra.entity.Entity;

public class BlockGenerator {

  @Inject GameController gameController;

  @Inject
  BlockGenerator() {}

  public Entity generate(Coordinates coordinates) {
    Entity block;
    if (coordinates.getY() > 0) {
      block = gameController.createSkyBlock(coordinates);
    } else if (Math.random() < 0.1) {
      block = gameController.createStoneBlock(coordinates);
    } else {
      block = gameController.createDirtBlock(coordinates);
    }
    return block;
  }
}

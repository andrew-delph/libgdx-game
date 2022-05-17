package generation;

import app.GameController;
import com.google.inject.Inject;
import common.exceptions.ChunkNotFound;
import entity.EntityFactory;
import entity.attributes.Coordinates;
import entity.block.BlockFactory;

public class BlockGenerator {

  @Inject BlockFactory blockFactory;
  @Inject EntityFactory entityFactory;
  @Inject GameController gameController;

  @Inject
  BlockGenerator() {}

  public void generate(Coordinates coordinates) throws ChunkNotFound {
    if (coordinates.getY() > 0) {
      gameController.triggerAddEntity(blockFactory.createSky(coordinates));
    } else if (Math.random() < 0.1) {
      gameController.triggerAddEntity(blockFactory.createStone(coordinates));
    } else if (Math.random() < 0.1) {
      gameController.triggerAddEntity(entityFactory.createOrb(coordinates));
      gameController.triggerAddEntity(blockFactory.createSky(coordinates));
    } else {
      gameController.triggerAddEntity(blockFactory.createDirt(coordinates));
    }
  }
}

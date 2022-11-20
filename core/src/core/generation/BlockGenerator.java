package core.generation;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.Coordinates;
import core.common.exceptions.ChunkNotFound;
import core.entity.EntityFactory;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.entity.misc.Orb;
import core.entity.misc.Sand;
import core.entity.misc.water.WaterPosition;

public class BlockGenerator {

  @Inject BlockFactory blockFactory;
  @Inject EntityFactory entityFactory;
  @Inject GameController gameController;

  @Inject
  BlockGenerator() {}

  public void generate(Coordinates coordinates) throws ChunkNotFound {

    gameController.triggerAddEntity(blockFactory.createSky(coordinates));

    if (coordinates.getY() > 0) {
      if (coordinates.getY() == 5 && coordinates.getX() == 5) {
        for (int i = 0; i < 30; i++) {
          WaterPosition water = entityFactory.createWaterPosition(coordinates);

          gameController.triggerAddEntity(water);
        }
      }
    } else if (Math.random() < 0.1 && coordinates.getY() != 0) {
      Sand sand = entityFactory.createSand(coordinates);
      gameController.triggerAddEntity(sand);
    } else if (Math.random() < 0.1 && coordinates.getY() != 0) {
      Orb orb = entityFactory.createOrb(coordinates);
      gameController.triggerAddEntity(orb);
    } else {
      Block block;
      if (coordinates.getY() == 0) {
        block = blockFactory.createStone(coordinates);
      } else if (Math.random() < 0.1) {
        block = blockFactory.createStone(coordinates);
      } else {
        block = blockFactory.createDirt(coordinates);
      }
      gameController.triggerAddEntity(block);
    }
  }
}

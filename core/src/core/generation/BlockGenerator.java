package core.generation;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.Coordinates;
import core.common.exceptions.ChunkNotFound;
import core.entity.EntityFactory;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.misc.Orb;
import core.entity.misc.Sand;
import core.entity.misc.water.WaterPosition;

public class BlockGenerator {

  @Inject BlockFactory blockFactory;
  @Inject EntityFactory entityFactory;
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;

  @Inject
  BlockGenerator() {}

  public void generate(Coordinates coordinates) throws ChunkNotFound {

    gameController.triggerAddEntity(blockFactory.createSky(coordinates));

    if (coordinates.getY() > 0) {
      if (coordinates.getY() == 5 && coordinates.getX() == 5) {
        for (int i = 0; i < 30; i++) {
          WaterPosition water = entityFactory.createWaterPosition(coordinates);

          water.setEntityController(entityControllerFactory.createWaterPositionController(water));
          gameController.triggerAddEntity(water);
        }
      }
    } else if (Math.random() < 0.1 && coordinates.getY() != 0) {
      gameController.triggerAddEntity(blockFactory.createSky(coordinates));
      Sand sand = entityFactory.createSand(coordinates);
      sand.setEntityController(entityControllerFactory.createSandController(sand));
      gameController.triggerAddEntity(sand);
    } else if (Math.random() < 0.1 && coordinates.getY() != 0) {
      Orb orb = entityFactory.createOrb(coordinates);
      orb.setEntityController(entityControllerFactory.createOrbController(orb));
      gameController.triggerAddEntity(orb);
      gameController.triggerAddEntity(blockFactory.createSky(coordinates));
    } else {
      Block block;
      if (coordinates.getY() == 0) {
        block = blockFactory.createStone(coordinates);
      } else if (Math.random() < 0.1) {
        block = blockFactory.createStone(coordinates);
      } else {
        block = blockFactory.createDirt(coordinates);
      }
      block.setEntityController(entityControllerFactory.createSolidBlockController(block));
      gameController.triggerAddEntity(block);
    }
  }
}

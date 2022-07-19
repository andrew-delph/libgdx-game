package core.generation;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.exceptions.ChunkNotFound;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.BlockFactory;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.misc.Orb;

public class BlockGenerator {

  @Inject BlockFactory blockFactory;
  @Inject EntityFactory entityFactory;
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;

  @Inject
  BlockGenerator() {}

  public void generate(Coordinates coordinates) throws ChunkNotFound {
    if (coordinates.getY() > 0) {
      gameController.triggerAddEntity(blockFactory.createSky(coordinates));
    } else if (Math.random() < 0.1) {
      gameController.triggerAddEntity(blockFactory.createStone(coordinates));
    } else if (Math.random() < 0.1) {
      Orb orb = entityFactory.createOrb(coordinates);
      orb.setEntityController(entityControllerFactory.createOrbController(orb));
      gameController.triggerAddEntity(orb);
      gameController.triggerAddEntity(blockFactory.createSky(coordinates));
    } else {
      gameController.triggerAddEntity(blockFactory.createDirt(coordinates));
    }
  }
}

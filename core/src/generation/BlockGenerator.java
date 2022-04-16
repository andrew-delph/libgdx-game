package generation;

import com.google.inject.Inject;
import common.Coordinates;
import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.block.BlockFactory;

public class BlockGenerator {

  @Inject BlockFactory blockFactory;

  @Inject
  BlockGenerator() {}

  public Entity generate(Coordinates coordinates) throws ChunkNotFound {
    if (coordinates.getY() > 0) {
      return blockFactory.createSky(coordinates);
    } else if (Math.random() < 0.1) {
      return blockFactory.createStone(coordinates);
    } else {
      return blockFactory.createDirt(coordinates);
    }
  }
}

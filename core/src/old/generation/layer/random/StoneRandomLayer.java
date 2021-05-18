package old.generation.layer.random;

import com.google.inject.Inject;
import old.generation.layer.AbstractLayer;
import old.infra.common.Coordinate;
import old.infra.map.block.BlockFactory;
import old.infra.map.chunk.Chunk;
import old.infra.map.chunk.ChunkRange;

import java.util.Random;

public class StoneRandomLayer extends AbstractLayer {

  @Inject BlockFactory blockFactory;

  @Override
  public void generateLayer(Chunk chunk) {
    ChunkRange chunkRange = chunk.chunkRange;
    for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
      for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000);
        if (rand_int1 < 100) {
          chunk.addBlock(blockFactory.createStoneBlock(new Coordinate(i, j)));
        }
      }
    }
  }
}

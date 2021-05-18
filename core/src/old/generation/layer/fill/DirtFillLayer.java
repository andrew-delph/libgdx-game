package old.generation.layer.fill;

import com.google.inject.Inject;
import old.generation.layer.AbstractLayer;
import old.infra.common.Coordinate;
import old.infra.map.block.BlockFactory;
import old.infra.map.chunk.Chunk;
import old.infra.map.chunk.ChunkRange;

import java.util.Random;

public class DirtFillLayer extends AbstractLayer {

  @Inject BlockFactory blockFactory;

  @Override
  public void generateLayer(Chunk chunk) {
    ChunkRange chunkRange = chunk.chunkRange;
    for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
      for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
        Random rand = new Random();
        chunk.addBlock(blockFactory.createDirtBlock(new Coordinate(i, j)));
      }
    }
  }
}

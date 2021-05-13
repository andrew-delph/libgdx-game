package generation.layer.fill;

import com.google.inject.Inject;
import generation.layer.AbstractLayer;
import infra.common.Coordinate;
import infra.map.block.BlockFactory;
import infra.map.chunk.Chunk;
import infra.map.chunk.ChunkRange;

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

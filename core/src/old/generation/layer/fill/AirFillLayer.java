package old.generation.layer.fill;

import com.google.inject.Inject;
import old.generation.layer.AbstractLayer;
import old.generation.noise.FastNoiseGenerator;
import old.infra.common.Coordinate;
import old.infra.map.block.BlockFactory;
import old.infra.map.chunk.Chunk;
import old.infra.map.chunk.ChunkRange;

public class AirFillLayer extends AbstractLayer {

  FastNoiseGenerator noise;

  @Inject BlockFactory blockFactory;

  public AirFillLayer() {
    noise = new FastNoiseGenerator();
    noise.setTopRange(5);
    noise.setyScale(7);
    noise.setxScale(7);
  }

  @Override
  public void generateLayer(Chunk chunk) {
    ChunkRange chunkRange = chunk.chunkRange;
    for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
      for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
        if (j > 50) {
          chunk.addBlock(blockFactory.createAirBlock(new Coordinate(i, j)));
        }
      }
    }
  }
}

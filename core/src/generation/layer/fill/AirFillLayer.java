package generation.layer.fill;

import com.google.inject.Inject;
import generation.layer.AbstractLayer;
import generation.noise.FastNoiseGenerator;
import infra.common.Coordinate;
import infra.map.block.BlockFactory;
import infra.map.chunk.Chunk;
import infra.map.chunk.ChunkRange;

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

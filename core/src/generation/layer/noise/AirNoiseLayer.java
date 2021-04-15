package generation.layer.noise;

import com.google.inject.Inject;
import generation.layer.AbstractLayer;
import generation.noise.FastNoiseGenerator;
import infra.common.Coordinate;
import infra.map.block.BlockFactory;
import infra.map.chunk.Chunk;
import infra.map.chunk.ChunkRange;

import java.util.Random;

public class AirNoiseLayer extends AbstractLayer {

    FastNoiseGenerator noise;

    @Inject
    BlockFactory blockFactory;

    public AirNoiseLayer() {
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
                int noise_value = noise.getValue(i,j);
                if(noise_value==4 || j > 50) {
                    chunk.addBlock(blockFactory.createAirBlock(new Coordinate(i, j)));
                }

            }
        }
    }
}

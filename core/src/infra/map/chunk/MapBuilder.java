package infra.map.chunk;

import com.google.inject.Inject;
import generation.noise.FastNoiseLite;
import infra.common.Coordinate;
import infra.map.block.BlockFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MapBuilder {

    @Inject
    BlockFactory blockFactory;

    FastNoiseLite noise;

    public MapBuilder() {
        noise = new FastNoiseLite(ThreadLocalRandom.current().nextInt());
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
    }

    public void generateDirt(Chunk chunk){
        ChunkRange chunkRange = chunk.chunkRange;
        for (int i =chunkRange.bottom_x; i < chunkRange.top_x; i++) {
            for (int j = chunkRange.bottom_y; j <chunkRange.top_y;j++){
                Random rand = new Random();
                int rand_int1 = rand.nextInt(1000);
                if(rand_int1<100){
                    chunk.addBlock(blockFactory.createBlock(new Coordinate(i,j)));
                }
                else{
                    chunk.addBlock(blockFactory.createDirtBlock(new Coordinate(i,j)));
                }
            }
        }
        chunk.generated = true;
    }

    public void generateWorld(Chunk chunk){
        ChunkRange chunkRange = chunk.chunkRange;
        for (int i =chunkRange.bottom_x; i < chunkRange.top_x; i++) {
            for (int j = chunkRange.bottom_y; j <chunkRange.top_y;j++){
                float noise_float = noise.GetNoise(i,j);
                if(noise_float<0){
                    chunk.addBlock(blockFactory.createBlock(new Coordinate(i,j)));
                }
                else{
                    chunk.addBlock(blockFactory.createDirtBlock(new Coordinate(i,j)));
                }
            }
        }
        chunk.generated = true;
    }
}

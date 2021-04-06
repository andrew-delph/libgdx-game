package infra.map.chunk;

import com.google.inject.Inject;
import infra.map.block.BlockFactory;

public class MapBuilder {

    @Inject
    BlockFactory blockFactory;

    public void generateDirt(Chunk chunk){
        ChunkRange chunkRange = chunk.chunkRange;
        for (int i =chunkRange.bottom_x; i < chunkRange.top_x; i++) {
            for (int j = chunkRange.bottom_y; j <chunkRange.top_y;j++){
                chunk.addBlock(blockFactory.createBlock(i,j));
            }
        }
        chunk.generated = true;
    }
}

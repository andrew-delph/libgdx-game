package infra.map.chunk;

import infra.common.Coordinate;
import infra.map.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Chunk {
    static final int size = 50;
    Map<Coordinate, Block> blockMap;
    public ChunkRange chunkRange;
    public Chunk(ChunkRange chunkRange){
        this.chunkRange = chunkRange;
        this.blockMap  = new HashMap<>();
    }

    public void addBlock(Block block) {
        this.blockMap.put(block.coordinate, block);
    }

    public Block getBlock(Coordinate coordinate) {
        return this.blockMap.get(coordinate);
    }
}

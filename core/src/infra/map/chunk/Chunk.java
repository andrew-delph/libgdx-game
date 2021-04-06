package infra.map.chunk;

import infra.common.Coordinate;
import infra.map.block.Block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chunk {
    public static final int size = 50;
    Map<Coordinate, Block> blockMap;
    public ChunkRange chunkRange;
    public Boolean generated = false;

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

    public List<Block> getBlocks(){
        List blocks = Arrays.asList(this.blockMap.values().toArray());
        return blocks;
    }
}

package old.infra.map.chunk;

import old.infra.common.Coordinate;
import old.infra.map.block.Block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chunk {
  public static final int size = 50;
  public ChunkRange chunkRange;
  public Boolean generated = false;
  Map<Coordinate, Block> blockMap;

  public Chunk(ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
    this.blockMap = new HashMap<>();
  }

  public void addBlock(Block block) {
    // TODO we can add blocks outside the chunk
    this.blockMap.put(block.coordinate, block);
  }

  public Block getBlock(Coordinate coordinate) {
    return this.blockMap.get(coordinate);
  }

  public List<Block> getBlocks() {
    List blocks = Arrays.asList(this.blockMap.values().toArray());
    return blocks;
  }
}

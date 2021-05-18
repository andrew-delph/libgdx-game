package old.infra.map;

import old.infra.common.Coordinate;
import old.infra.map.block.Block;
import old.infra.map.chunk.Chunk;
import old.infra.map.chunk.ChunkRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapGrid {
  Map<ChunkRange, Chunk> chunkMap;

  public MapGrid() {
    this.chunkMap = new HashMap<>();
  }

  void addBlock(Block block) {
    if (this.chunkMap.get(new ChunkRange(block.coordinate)) == null) {
      this.chunkMap.put(
          new ChunkRange(block.coordinate), new Chunk(new ChunkRange(block.coordinate)));
    }
    this.chunkMap.get(new ChunkRange(block.coordinate)).addBlock(block);
  }

  Block getBlock(Coordinate coordinate) {
    if (coordinate == null || this.chunkMap.get(new ChunkRange(coordinate)) == null) {
      return null;
    }
    return this.chunkMap.get(new ChunkRange(coordinate)).getBlock(coordinate);
  }

  public Chunk getChunk(Coordinate coordinate) {
    ChunkRange chunkRange = new ChunkRange(coordinate);
    if (this.chunkMap.get(chunkRange) == null) {
      this.chunkMap.put(chunkRange, new Chunk(chunkRange));
    }
    return this.chunkMap.get(chunkRange);
  }

  List<Block> getBlocksInRange(int x, int y, int width, int height) {
    ArrayList<Block> blocksList = new ArrayList();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Coordinate getCoordinate = new Coordinate(x + i, y + j);
        blocksList.add(this.getBlock(getCoordinate));
      }
    }
    return blocksList;
  }
}

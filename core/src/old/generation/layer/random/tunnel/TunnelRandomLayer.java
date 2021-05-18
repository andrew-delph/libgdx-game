package old.generation.layer.random.tunnel;

import com.google.inject.Inject;
import old.generation.layer.AbstractLayer;
import old.infra.common.Coordinate;
import old.infra.map.block.BlockFactory;
import old.infra.map.chunk.Chunk;
import old.infra.map.chunk.ChunkRange;

import java.util.*;

public class TunnelRandomLayer extends AbstractLayer {

  @Inject BlockFactory blockFactory;

  Set<Coordinate> coordinateSet = new HashSet<>();

  Set<ChunkRange> generatedChunks = new HashSet<>();

  @Override
  public void generateLayer(Chunk chunk) {
    // generate tunnels for each chunk
    // for each generated tunnel. attach it to a chunk
    if (!this.generatedChunks.contains(chunk.chunkRange)) {
      List<Tunnel> tunnelList = this.generateTunnelsOnChunk(chunk);
      for (Tunnel tunnel : tunnelList) {
        this.coordinateSet.addAll(tunnel.generate());
      }
    }

    // add blocks to the chunk
    ChunkRange chunkRange = chunk.chunkRange;
    for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
      for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
        Coordinate current = new Coordinate(i, j);
        if (this.coordinateSet.contains(current)) {
          chunk.addBlock(blockFactory.createAirBlock(current));
        }
      }
    }
  }

  List<Tunnel> generateTunnelsOnChunk(Chunk chunk) {
    List<Tunnel> tunnelList = new LinkedList<>();
    this.generatedChunks.add(chunk.chunkRange);
    for (int i = chunk.chunkRange.bottom_x; i < chunk.chunkRange.top_x; i++) {
      for (int j = chunk.chunkRange.bottom_y; j < chunk.chunkRange.top_y; j++) {
        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000);
        if (rand_int1 < 10) {
          chunk.addBlock(blockFactory.createAirBlock(new Coordinate(i, j)));
          tunnelList.add(new Tunnel(rand.nextInt(99999999), new Coordinate(i, j)));
        }
      }
    }
    return tunnelList;
  }
}

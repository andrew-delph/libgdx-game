package infra.generation;

import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.common.GameStore;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenerationManager {
  Set<ChunkRange> generatedSet;

  @Inject GameStore gameStore;

  @Inject ChunkBuilderFactory chunkBuilderFactory;

  GenerationManager() {
    this.generatedSet = new HashSet<>();
  }

  Boolean isGenerated(ChunkRange chunkRange) {
    return this.generatedSet.contains(chunkRange);
  }

  ChunkBuilder generate(ChunkRange chunkRange) {
    this.generatedSet.add(chunkRange);
    return chunkBuilderFactory.create(chunkRange);
  }

  public List<Callable<Chunk>> generateAround(ChunkRange chunkRangeRoot) {
    List<ChunkRange> surroundingChunkRangeList = new LinkedList<>();

    surroundingChunkRangeList.add(chunkRangeRoot.getLeft());
    surroundingChunkRangeList.add(chunkRangeRoot.getRight());
    surroundingChunkRangeList.add(chunkRangeRoot.getUp());
    surroundingChunkRangeList.add(chunkRangeRoot.getDown());
    surroundingChunkRangeList.add(chunkRangeRoot.getLeft().getUp());
    surroundingChunkRangeList.add(chunkRangeRoot.getLeft().getDown());
    surroundingChunkRangeList.add(chunkRangeRoot.getRight().getUp());
    surroundingChunkRangeList.add(chunkRangeRoot.getRight().getDown());

    List<Callable<Chunk>> chunkBuilderList = new LinkedList<>();

    for (ChunkRange chunkRange : surroundingChunkRangeList) {
      if(!isGenerated(chunkRange)){
        chunkBuilderList.add(generate(chunkRange));
      }
    }
    return chunkBuilderList;
  }
}

package infra.generation;

import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.common.GameStore;
import infra.entity.Entity;

import java.util.*;
import java.util.concurrent.Callable;

public class ChunkGenerationManager {
  Set<ChunkRange> generatedSet;
  Set<Entity> activeEntity;

  @Inject GameStore gameStore;

  @Inject ChunkBuilderFactory chunkBuilderFactory;

  ChunkGenerationManager() {
    this.generatedSet = new HashSet<>();
    this.activeEntity = new HashSet<>();
  }

  public void registerActiveEntity(Entity entity){
    this.activeEntity.add(entity);
  }

  public List<Entity> getActiveEntityList(){
    return new ArrayList<>(this.activeEntity);
  }

  public List<Callable<Chunk>> generateActiveEntities(){
    List<Callable<Chunk>> generationList = new LinkedList<>();
    for (Entity entity : this.getActiveEntityList()) {
      generationList.addAll(generateAround(new ChunkRange(entity.coordinates)));
    }
    return generationList;
  }

  Boolean isGenerated(ChunkRange chunkRange) {
    return this.generatedSet.contains(chunkRange);
  }

  public ChunkBuilder generate(ChunkRange chunkRange) {
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

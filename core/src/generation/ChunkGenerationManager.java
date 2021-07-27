package generation;

import com.google.inject.Inject;
import chunk.Chunk;
import chunk.ChunkRange;
import common.Coordinates;
import common.GameStore;
import entity.Entity;

import java.util.*;
import java.util.concurrent.Callable;

public class ChunkGenerationManager {
  Set<ChunkRange> generatedSet;
  Set<Entity> activeEntity;
  Map<UUID, List<UUID>> uuidOwnerMap;

  @Inject GameStore gameStore;
  @Inject ChunkBuilderFactory chunkBuilderFactory;

  @Inject
  ChunkGenerationManager() {
    this.generatedSet = new HashSet<>();
    this.activeEntity = new HashSet<>();
    this.uuidOwnerMap = new HashMap<>();
  }

  public void registerActiveEntity(Entity entity, UUID uuid) {
    this.activeEntity.add(entity);
    this.uuidOwnerMap.computeIfAbsent(uuid, k -> new LinkedList<>());
    this.uuidOwnerMap.get(uuid).add(entity.uuid);
  }

  public List<UUID> getOwnerUuidList(UUID uuid) {
    this.uuidOwnerMap.computeIfAbsent(uuid, k -> new LinkedList<>());
    return this.uuidOwnerMap.get(uuid);
  }

  public List<Entity> getActiveEntityList() {
    return new ArrayList<>(this.activeEntity);
  }

  public List<Callable<Chunk>> generateActiveEntities() {
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

    List<ChunkRange> surroundingChunkRangeList =
        ChunkRange.getChunkRangeListAroundPoint(
            new Coordinates(chunkRangeRoot.bottom_x, chunkRangeRoot.bottom_y), 3);

    List<Callable<Chunk>> chunkBuilderList = new LinkedList<>();

    for (ChunkRange chunkRange : surroundingChunkRangeList) {
      if (!isGenerated(chunkRange)) {
        chunkBuilderList.add(generate(chunkRange));
      }
    }
    return chunkBuilderList;
  }
}

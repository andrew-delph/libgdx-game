package infra.common;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.entity.Entity;
import infra.entity.block.Block;
import infra.entity.misc.Ladder;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class GameStore {

  Map<UUID, ChunkRange> entityMap;

  @Inject ChunkClockMap chunkClockMap;

  @Inject
  GameStore() {
    this.entityMap = new ConcurrentHashMap<>();
  }

  public void addEntity(Entity entity) {
    ChunkRange entityChunkRange = new ChunkRange(entity.coordinates);
    this.chunkClockMap.get(entityChunkRange).addEntity(entity);
    this.entityMap.put(entity.uuid, entityChunkRange);
  }

  public Entity removeEntity(UUID uuid) {
    ChunkRange chunkRange = this.entityMap.get(uuid);
    if (chunkRange == null) return null;
    Chunk chunk = this.chunkClockMap.get(chunkRange);
    if (chunk == null) return null;
    Entity entity = chunk.removeEntity(uuid);
    this.entityMap.remove(uuid);
    return entity;
  }

  public int getEntityNumber() {
    return this.entityMap.size();
  }

  public Entity getEntity(UUID uuid) {
    ChunkRange chunkRange = this.entityMap.get(uuid);
    if (chunkRange == null) {
      return null;
    }
    Chunk chunk = this.chunkClockMap.get(chunkRange);
    if (chunk == null) {
      return null;
    }
    return chunk.getEntity(uuid);
  }

  public Chunk getEntityChunk(UUID uuid) {
    return this.chunkClockMap.get(this.entityMap.get(uuid));
  }

  public void addChunk(Chunk chunk) {
    this.chunkClockMap.add(chunk);
  }

  public Chunk getChunk(ChunkRange chunkRange) {
    return this.chunkClockMap.get(chunkRange);
  }

  public void syncEntity(Entity entity) {
    if (!entityMap.get(entity.uuid).equals(new ChunkRange(entity.coordinates))) {
      Vector2 velocity = entity.getBody().getLinearVelocity();
      this.removeEntity(entity.uuid);
      this.addEntity(entity);
      entity.getBody().setLinearVelocity(velocity);
    }
  }

  public List<Entity> getEntityListInRange(int x1, int y1, int x2, int y2) {
    List<Entity> entityList = new ArrayList<>();
    for (UUID entityUUID : new ArrayList<>(this.entityMap.keySet())) {
      entityList.add(this.getEntity(entityUUID));
    }
    return entityList;
  }

  public Set<ChunkRange> getActiveChunkRangeSet() {
    return new HashSet<>(this.entityMap.values());
  }

  public List<Callable<Chunk>> getChunkOnClock(Tick tick) {
    return this.chunkClockMap.getChunksOnTick(tick);
  }

  public Block getBlock(Coordinates coordinates) {
    return this.chunkClockMap.get(new ChunkRange(coordinates)).getBlock(coordinates.getBase());
  }

  public Ladder getLadder(Coordinates coordinates) {
    return this.chunkClockMap.get(new ChunkRange(coordinates)).getLadder(coordinates.getBase());
  }

  public List<Block> getBlockInRange(
      Coordinates bottomLeftCoordinates, Coordinates topRightCoordinates) {
    List<Block> blockList = new LinkedList<>();
    for (Coordinates coordinates :
        Coordinates.getInRangeList(bottomLeftCoordinates, topRightCoordinates)) {
      blockList.add(this.getBlock(coordinates));
    }
    return blockList;
  }
}

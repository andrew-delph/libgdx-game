package common;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.block.Block;
import entity.misc.Ladder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import networking.events.EventTypeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStore {

  final Logger LOGGER = LogManager.getLogger();
  private final Map<UUID, ChunkRange> entityMap = new ConcurrentHashMap<>();
  @Inject ChunkClockMap chunkClockMap;
  @Inject EventService eventService;

  @Inject
  GameStore() {}

  public void addEntity(Entity entity) {
    ChunkRange entityChunkRange = new ChunkRange(entity.coordinates);
    this.entityMap.put(entity.uuid, entityChunkRange);
    this.chunkClockMap.get(entityChunkRange).addEntity(entity);
  }

  public Entity removeEntity(UUID uuid) throws EntityNotFound {
    ChunkRange chunkRange = this.entityMap.get(uuid);
    if (chunkRange == null) throw new EntityNotFound("UUID not found in entityMap");
    Chunk chunk = this.chunkClockMap.get(chunkRange);
    if (chunk == null) throw new EntityNotFound("UUID not found in entityMap");
    Entity entity = chunk.removeEntity(uuid);
    this.entityMap.remove(uuid);
    return entity;
  }

  public int getEntityNumber() {
    return this.entityMap.size();
  }

  public Entity getEntity(UUID uuid) throws EntityNotFound {
    ChunkRange chunkRange = this.entityMap.get(uuid);
    if (chunkRange == null) {
      throw new EntityNotFound("UUID #" + uuid + " not in entityMap");
    }
    Chunk chunk = this.chunkClockMap.get(chunkRange);
    if (chunk == null) {
      throw new EntityNotFound("Chunk is null");
    }
    return chunk.getEntity(uuid);
  }

  public Boolean doesEntityExist(UUID uuid) {
    try {
      this.getEntity(uuid);
    } catch (EntityNotFound e) {
      return false;
    }
    return true;
  }

  public Boolean doesChunkExist(ChunkRange chunkRange) {
    return this.chunkClockMap.doesChunkExist(chunkRange);
  }

  public Chunk getEntityChunk(UUID uuid) {
    return this.chunkClockMap.get(this.entityMap.get(uuid));
  }

  public void addChunk(Chunk chunk) {
    for (Entity entity : chunk.getEntityList()) {
      this.entityMap.put(entity.uuid, chunk.chunkRange);
    }
    this.chunkClockMap.add(chunk);
  }

  public void removeChunk(ChunkRange chunkRange) {
    Chunk removed = this.chunkClockMap.get(chunkRange);
    for (UUID uuidToRemove : removed.getEntityUUIDSet()) {
      this.entityMap.remove(uuidToRemove);
    }
    this.chunkClockMap.remove(chunkRange);
  }

  public Chunk getChunk(ChunkRange chunkRange) {
    return this.chunkClockMap.get(chunkRange);
  }

  public List<Entity> getEntityListInRange(int x1, int y1, int x2, int y2) {
    List<Entity> entityList = new ArrayList<>();
    for (UUID entityUUID : new ArrayList<>(this.entityMap.keySet())) {
      try {
        entityList.add(this.getEntity(entityUUID));
      } catch (EntityNotFound e) {
        LOGGER.error(e);
      }
    }
    return entityList;
  }

  public List<Entity> getEntityListFromList(List<UUID> uuidList) {
    List<Entity> entityList = new ArrayList<>();
    for (UUID entityUUID : uuidList) {
      try {
        entityList.add(this.getEntity(entityUUID));
      } catch (EntityNotFound e) {
        LOGGER.error(e);
      }
    }
    return entityList;
  }

  public Set<ChunkRange> getActiveChunkRangeSet() {
    return new HashSet<>(this.entityMap.values());
  }

  public List<Callable<Chunk>> getChunkOnClock(Tick tick) {
    return this.chunkClockMap.getChunksOnTick(tick);
  }

  public Block getBlock(Coordinates coordinates) throws EntityNotFound {
    Chunk chunk = this.chunkClockMap.get(new ChunkRange(coordinates));
    if (chunk == null) throw new EntityNotFound("Chunk not found");
    return chunk.getBlock(coordinates.getBase());
  }

  public Ladder getLadder(Coordinates coordinates) {
    return this.chunkClockMap.get(new ChunkRange(coordinates)).getLadder(coordinates.getBase());
  }

  public List<Entity> getEntityInRange(
      Coordinates bottomLeftCoordinates, Coordinates topRightCoordinates) {
    List<Entity> entityList = new LinkedList<>();

    List<ChunkRange> ChunkRangeInRange =
        ChunkRange.getChunkRangeListTwoPoints(bottomLeftCoordinates, topRightCoordinates);

    for (ChunkRange currentChunkRange : ChunkRangeInRange) {
      Chunk currentChunk = this.getChunk(currentChunkRange);
      if (currentChunk == null) continue;
      entityList.addAll(currentChunk.getEntityInRange(bottomLeftCoordinates, topRightCoordinates));
    }

    return entityList;
  }

  public List<Entity> getEntityListBaseCoordinates(Coordinates coordinates) {
    coordinates = coordinates.getBase();
    return this.chunkClockMap
        .get(new ChunkRange(coordinates))
        .getEntityListBaseCoordinates(coordinates);
  }

  public ChunkRange getEntityChunkRange(UUID uuid) throws EntityNotFound {
    if (entityMap.get(uuid) == null)
      throw new EntityNotFound("No chunk range found for Entity #uuid" + uuid);
    return entityMap.get(uuid);
  }

  public Set<ChunkRange> getChunkRangeList() {
    return this.chunkClockMap.getChunkRangeSet();
  }

  public synchronized void syncEntity(Entity entity) throws EntityNotFound {
    UUID target = entity.uuid;
    ChunkRange from = this.getEntityChunkRange(entity.uuid);
    ChunkRange to = new ChunkRange(entity.coordinates);
    if (!from.equals(to)) {
      this.eventService.queuePostUpdateEvent(
          EventTypeFactory.createReplaceEntityEvent(entity.uuid, entity, true, to));
      this.eventService.fireEvent(
          EventTypeFactory.createChunkSwapOutgoingEventType(target, from, to));
    }
  }
}

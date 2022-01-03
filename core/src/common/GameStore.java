package common;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.block.Block;
import entity.misc.Ladder;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GameStore {

    private final static Logger LOGGER = Logger.getLogger(GameStore.class.getName());

    Map<UUID, ChunkRange> entityMap;

    @Inject
    ChunkClockMap chunkClockMap;

    @Inject
    GameStore() {
        this.entityMap = new ConcurrentHashMap<>();
    }

    public void addEntity(Entity entity) {
        ChunkRange entityChunkRange = new ChunkRange(entity.coordinates);
        this.chunkClockMap.get(entityChunkRange).addEntity(entity);
        this.entityMap.put(entity.uuid, entityChunkRange);
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
            throw new EntityNotFound("UUID not in entityMap");
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

    public Chunk getEntityChunk(UUID uuid) {
        return this.chunkClockMap.get(this.entityMap.get(uuid));
    }

    public void addChunk(Chunk chunk) {
        this.chunkClockMap.add(chunk);
        for (Entity entity : chunk.getEntityList()) {
            this.entityMap.put(entity.uuid, chunk.chunkRange);
        }
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
                LOGGER.fine(e.toString());
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
                LOGGER.fine(e.toString());
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

    public List<Block> getBlockInRange(
            Coordinates bottomLeftCoordinates, Coordinates topRightCoordinates) {
        List<Block> blockList = new LinkedList<>();
        for (Coordinates coordinates :
                Coordinates.getInRangeList(bottomLeftCoordinates, topRightCoordinates)) {
            try {
                blockList.add(this.getBlock(coordinates));
            } catch (EntityNotFound e) {
                LOGGER.fine(e.toString());
            }
        }
        return blockList;
    }

    public List<Entity> getEntityListBaseCoordinates(Coordinates coordinates) {
        coordinates = coordinates.getBase();
        return this.chunkClockMap
                .get(new ChunkRange(coordinates))
                .getEntityListBaseCoordinates(coordinates);
    }

    public ChunkRange getEntityChunkRange(UUID uuid) {
        return entityMap.get(uuid);
    }
}

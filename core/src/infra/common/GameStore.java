package infra.common;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameStore {

    Map<UUID, Entity> entityMap;
    Map<ChunkRange, Chunk> chunkMap;
    Map<Integer,List<Chunk>> clockMap;
    Body body;
    Sprite sprite;

    GameStore(){
        this.entityMap = new HashMap<>();
        this.chunkMap = new HashMap<>();
        this.clockMap = new HashMap<>();
    }

    public void addEntity(Entity entity){

    }

    public Entity getEntity(UUID uuid){
        return this.entityMap.get(uuid);
    }

    public Chunk getChunk(ChunkRange chunkRange){
        return this.chunkMap.get(chunkRange);
    }

    public List<Entity> getEntityListInRange(int x1, int y1, int x2, int y2){
        return null;
    }

    public List<Chunk> getChunkOnClock(int clock){
        return this.clockMap.get(Integer.valueOf(clock));
    }

}

package infra.chunk;

import com.badlogic.gdx.physics.box2d.World;
import infra.common.networkobject.Coordinates;
import infra.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class Chunk implements Callable<Chunk> {
    World world;
    Map<UUID, Entity> chunkMap;

    int updateTimeout;

    Chunk(){
        this.chunkMap = new HashMap();
    }

    public int getUpdateTimeout(){
        return this.updateTimeout;
    }

    @Override
    public Chunk call() throws Exception {
        return null;
    }

    void update(){
        updateTimeout = Integer.MAX_VALUE;
        for (Entity entity :this.chunkMap.values()) {
            entity.controller.update();
            int entityTimeout = entity.getUpdateTimeout();
            if (this.updateTimeout<entityTimeout){
                this.updateTimeout = entityTimeout;
            }
        }
    }
}

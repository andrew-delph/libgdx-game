package infra;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class EntityManager {

    static EntityManager singleton;

    public static EntityManager getInstance(){
        if (singleton == null){
            singleton = new EntityManager();
        }
        return singleton;
    }

    private final Map<UUID,Entity> entityMap;

    private EntityManager() {
        entityMap = new HashMap<>();
    }

    public void add(Entity entity){
        this.entityMap.put(entity.id,entity);
    }

    public void updateAll(SpriteBatch batch){
        for(Map.Entry<UUID,Entity> entry: this.entityMap.entrySet()){
            entry.getValue().update(batch);
        }
    }

}

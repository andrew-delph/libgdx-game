package infra.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.UUID;

public class EntityManager {

    static EntityManager instance;
    HashMap<UUID, Entity> entityMap;

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    EntityManager() {
        this.entityMap = new HashMap();
    }

    public void add(Entity data) {
        this.entityMap.put(data.data.getID(), data);
    }

    public Entity get(UUID id) {
        return this.entityMap.get(id);
    }

    public Entity[] getAll() {
        return this.entityMap.values().toArray(new Entity[0]);
    }

    public void update(SpriteBatch batch) {
        for (Entity entity : this.entityMap.values()) {
            entity.update(batch);
        }
    }
}

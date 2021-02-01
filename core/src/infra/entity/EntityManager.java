package infra.entity;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class EntityManager {

    static HashMap<UUID, EntityManager> managerMap = new HashMap<>();

    HashMap<String, Entity> entityMap;


    public static EntityManager getInstance(UUID id) {
        if (managerMap.get(id) == null) {
            managerMap.put(id, new EntityManager());
        }
        return managerMap.get(id);
    }

    EntityManager() {
        System.out.println("new manager,");
        this.entityMap = new HashMap();
    }

    public void add(Entity data) {
        System.out.println("adding: "+data.data.getID());
        this.entityMap.put(data.data.getID(), data);
    }

    public Entity get(UUID id) {
        System.out.println("getting:"+id.toString());
        return this.entityMap.get(id.toString());
    }

    public Entity[] getAll() {
        return this.entityMap.values().toArray(new Entity[0]);
    }

    public void update(Consumer<Entity> entityConsumer) {
        for (Entity entity : this.entityMap.values()) {
            entityConsumer.accept(entity);
        }
    }
}

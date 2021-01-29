package infra.entity.factories;

import infra.entity.Entity;
import infra.entity.EntityData;

import java.util.UUID;

public class EntityFactory {
    static EntityFactory instance;

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }
        return instance;
    }

    private EntityFactory() {
    }

    public Entity create(EntityData data) {
        return new Entity(data);
    }

    public Entity create(UUID id, int x, int y) {
        return new Entity(id, x, y);
    }

    public Entity createBasic() {
        return new Entity(UUID.randomUUID(), 0, 0);
    }
}

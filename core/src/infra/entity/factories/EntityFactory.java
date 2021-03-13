package infra.entity.factories;

import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.EntityData;

import java.util.UUID;

public class EntityFactory {

    @Inject
    private EntityFactory() {
    }

    public Entity create(EntityData data) {
        return new Entity(data);
    }

    public Entity create(UUID id, int x, int y, UUID owner) {
        return new Entity(id, x, y, owner);
    }

    public Entity createBasic() {
        return new Entity(UUID.randomUUID(), 0, 0, UUID.randomUUID());
    }
}

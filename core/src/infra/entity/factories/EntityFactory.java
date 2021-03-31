package infra.entity.factories;

import base.BaseAssetManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.entity.Entity;
import infra.entity.EntityData;

import java.util.UUID;

public class EntityFactory {

    @Inject @Named("provideTexture")
    Boolean provideTexture;

    @Inject
    BaseAssetManager assetManager;

    public Entity create(EntityData data) {
        return new Entity(data);
    }

    public Entity create(UUID id, int x, int y, UUID owner) {
        if (provideTexture) {
            return new Entity(id, x, y, owner, assetManager.get("badlogic.jpg"));
        }
        else{
            return new Entity(id, x, y, owner);
        }
    }

    public Entity createBasic() {
        return new Entity(UUID.randomUUID(), 0, 0, UUID.randomUUID());
    }
}

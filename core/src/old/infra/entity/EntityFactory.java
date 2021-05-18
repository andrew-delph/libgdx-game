package old.infra.entity;

import old.base.BaseAssetManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import old.game.User;
import old.infra.entitydata.EntityData;
import old.infra.events.EventService;

import java.util.UUID;

public class EntityFactory {

  @Inject
  @Named("provideTexture")
  Boolean provideTexture;

  @Inject EventService eventService;

  @Inject BaseAssetManager assetManager;

  @Inject User user;

  public Entity create(EntityData data) {
    return new Entity(data);
  }

  public Entity create(UUID id, float x, float y, UUID owner) {
    if (provideTexture) {
      return new Entity(id, x, y, user.getId(), assetManager.get("badlogic.jpg"));
    } else {
      return new Entity(id, x, y, user.getId());
    }
  }

  public Entity createBasic() {
    Entity entity = new Entity(UUID.randomUUID(), 0, 0, user.getId());
    return entity;
  }
}

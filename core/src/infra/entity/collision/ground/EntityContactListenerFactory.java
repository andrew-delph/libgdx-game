package infra.entity.collision.ground;

import com.google.inject.Inject;
import infra.entity.collision.CollisionService;

public class EntityContactListenerFactory {
  @Inject CollisionService collisionService;

  @Inject
  EntityContactListenerFactory() {}

  public EntityContactListener createEntityContactListener() {
    return new EntityContactListener(collisionService);
  }
}

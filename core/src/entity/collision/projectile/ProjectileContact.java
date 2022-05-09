package entity.collision.projectile;

import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectileContact implements ContactWrapper {

  Set<UUID> projectileCollision = ConcurrentHashMap.newKeySet();

  public boolean isCollision(UUID uuid) {
    return projectileCollision.contains(uuid);
  }

  @Override
  public void beginContact(CollisionPoint source, CollisionPoint target) {
    projectileCollision.add(source.getEntity().getUuid());
    projectileCollision.add(target.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

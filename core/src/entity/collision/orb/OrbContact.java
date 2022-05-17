package entity.collision.orb;

import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OrbContact implements ContactWrapper {

  Set<UUID> orbCollision = ConcurrentHashMap.newKeySet();

  public boolean isCollision(UUID uuid) {
    return orbCollision.contains(uuid);
  }

  @Override
  public void beginContact(CollisionPoint source, CollisionPoint target) {
    orbCollision.add(source.getEntity().getUuid());
    orbCollision.add(target.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

package entity.collision.orb;

import com.google.inject.Inject;
import common.Clock;
import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrbContact implements ContactWrapper {

  final Map<UUID, List<UUID>> mapCollisionOrder = new HashMap<>();
  @Inject Clock clock;

  public synchronized void init() {
    clock.addTaskOnTick(mapCollisionOrder::clear);
  }

  public synchronized boolean isCollision(UUID orbID) {
    return mapCollisionOrder.containsKey(orbID);
  }

  public List<UUID> getCollisions(UUID orbID) {
    return mapCollisionOrder.get(orbID);
  }

  @Override
  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    // source is Orb
    // target is Entity

    mapCollisionOrder.putIfAbsent(source.getEntity().getUuid(), new LinkedList<>());
    mapCollisionOrder.get(source.getEntity().getUuid()).add(target.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

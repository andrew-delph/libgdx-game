package core.entity.collision;

import com.google.inject.Inject;
import core.common.Clock;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class CollisionTrackerContactWrapper implements ContactWrapper {

  final Map<UUID, List<UUID>> mapCollisionOrder = new HashMap<>();
  @Inject Clock clock;

  public synchronized void init() {
    clock.addTaskOnTick(mapCollisionOrder::clear);
  }

  public synchronized boolean isCollision(UUID orbID) {
    return mapCollisionOrder.containsKey(orbID);
  }

  public synchronized List<UUID> getCollisions(UUID orbID) {
    return new LinkedList<>(mapCollisionOrder.get(orbID));
  }

  @Override
  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    mapCollisionOrder.putIfAbsent(source.getEntity().getUuid(), new LinkedList<>());
    mapCollisionOrder.get(source.getEntity().getUuid()).add(target.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

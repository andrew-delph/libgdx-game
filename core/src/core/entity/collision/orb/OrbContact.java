package core.entity.collision.orb;

import core.entity.collision.CollisionPoint;
import core.entity.collision.CollisionTrackerContactWrapper;

public class OrbContact extends CollisionTrackerContactWrapper {

  @Override
  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    super.beginContact(source, target);
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {
    super.endContact(source, target);
  }
}

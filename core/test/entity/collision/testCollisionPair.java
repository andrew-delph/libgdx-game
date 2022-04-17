package entity.collision;

import entity.collision.ground.GroundPoint;
import org.junit.Test;

public class testCollisionPair {

  @Test
  public void testCollisionPairHash() {
    CollisionPair p1 = new CollisionPair(EntityPoint.class, GroundPoint.class);
    assert p1.equals(new CollisionPair(EntityPoint.class, GroundPoint.class));
    assert !p1.equals(new CollisionPair(GroundPoint.class, GroundPoint.class));
  }
}

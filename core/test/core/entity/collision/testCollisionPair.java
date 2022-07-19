package core.entity.collision;

import core.entity.collision.CollisionPair;
import core.entity.collision.EntitySensor;
import core.entity.collision.ground.GroundSensor;
import org.junit.Test;

public class testCollisionPair {

  @Test
  public void testCollisionPairHash() {
    CollisionPair p1 = new CollisionPair(EntitySensor.class, GroundSensor.class);
    assert p1.equals(new CollisionPair(EntitySensor.class, GroundSensor.class));
    assert !p1.equals(new CollisionPair(GroundSensor.class, GroundSensor.class));
  }
}

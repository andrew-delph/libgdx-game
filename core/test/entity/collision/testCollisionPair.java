package entity.collision;

import entity.Entity;
import entity.block.Block;
import org.junit.Test;

public class testCollisionPair {

  @Test
  public void testCollisionPairHash() {
    CollisionPair p1 = new CollisionPair(Entity.class, Block.class);
    assert p1.equals(new CollisionPair(Entity.class, Block.class));
    assert !p1.equals(new CollisionPair(Block.class, Block.class));
  }
}

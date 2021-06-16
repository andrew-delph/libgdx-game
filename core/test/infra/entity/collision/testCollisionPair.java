package infra.entity.collision;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import configuration.ClientConfig;
import configuration.SoloConfig;
import infra.entity.Entity;
import infra.entity.block.Block;
import org.junit.Test;

public class testCollisionPair {

  @Test
  public void testCollisionPairHash() {
    CollisionPair p1 = new CollisionPair(Entity.class, Block.class);
    assert p1.equals(new CollisionPair(Entity.class, Block.class));
    assert !p1.equals(new CollisionPair(Block.class, Block.class));

  }
}

package entity;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class testEntityEqual {
  @Test
  public void testIfEntityEqual() {
    Injector injector = Guice.createInjector(new ClientConfig());
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Entity entity1 = entityFactory.createEntity();
    Entity entity2 = entityFactory.createEntity();

    assert !entity1.equals(entity2);

    entity2.uuid = entity1.uuid;

    assert entity1.equals(entity2);
  }

  @Test
  public void testAddToSet() {
    Injector injector = Guice.createInjector(new ClientConfig());
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Entity entity1 = entityFactory.createEntity();
    Entity entity2 = entityFactory.createEntity();

    Set<Entity> entitySet = new HashSet<>();

    entitySet.add(entity1);
    entitySet.add(entity2);
    assert entitySet.size() == 2;
    entitySet.remove(entity1);
    assert entitySet.size() == 1;
    assert entitySet.contains(entity2);
    assert !entitySet.contains(entity1);
  }
}

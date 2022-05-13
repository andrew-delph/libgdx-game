package entity.attributes;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.BaseServerConfig;
import entity.Entity;
import entity.EntityFactory;
import org.junit.Before;
import org.junit.Test;

public class testEntityUpdates {
  EntityFactory entityFactory;

  @Before
  public void setup() {
    Injector injector = Guice.createInjector(new BaseServerConfig());
    entityFactory = injector.getInstance(EntityFactory.class);
  }

  @Test
  public void testCoordinatesUpdate() {
    Entity entity = entityFactory.createEntity(new Coordinates(0, 1));

    Coordinates newCoords = new Coordinates(2, 2);

    assert !entity.coordinates.equals(newCoords);

    entity.updateAttribute(newCoords);

    assert entity.coordinates.equals(newCoords);
  }

  @Test
  public void testHealthUpdate() {
    Entity entity = entityFactory.createEntity(new Coordinates(0, 1));

    Health newHealth = new Health(22);

    assert !entity.health.equals(newHealth);

    entity.updateAttribute(newHealth);

    assert entity.health.equals(newHealth);
  }
}

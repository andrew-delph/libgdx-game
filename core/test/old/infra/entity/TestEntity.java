package old.infra.entity;

import old.infra.entitydata.EntityData;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestEntity {

  @Test
  public void testEntityDefined() {
    UUID id = UUID.randomUUID();
    float x = 1;
    float y = 2;
    UUID owner = UUID.randomUUID();
    Entity testEntity = new Entity(id, x, y, owner);
    assertEquals(x, testEntity.getX(), 0.1);
    assertEquals(y, testEntity.getY(), 0.1);
    testEntity.moveX(1);
    testEntity.moveY(-1);
    assertEquals(x + 1, testEntity.getX(), 0.1);
    assertEquals(y - 1, testEntity.getY(), 0.1);
  }

  @Test
  public void testEntityEntityData() {
    UUID id = UUID.randomUUID();
    float x = 1;
    float y = 2;
    UUID owner = UUID.randomUUID();
    EntityData entityData = new EntityData();
    entityData.setId(id.toString());
    entityData.setX(String.valueOf(x));
    entityData.setY(String.valueOf(y));
    entityData.setOwner(owner.toString());
    Entity testEntity = new Entity(entityData);
    assertEquals(x, testEntity.getX(), 0.1);
    assertEquals(y, testEntity.getY(), 0.1);
    testEntity.moveX(1);
    testEntity.moveY(-1);
    assertEquals(x + 1, testEntity.getX(), 0.1);
    assertEquals(y - 1, testEntity.getY(), 0.1);
  }
}

package core.entity.attributes;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.screen.assets.animations.AnimationState;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.configuration.BaseServerConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.inventory.Equipped;
import core.entity.attributes.inventory.item.EmptyInventoryItem;
import core.entity.attributes.inventory.item.OrbInventoryItem;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.attributes.msc.CoordinatesWrapper;
import core.entity.attributes.msc.Health;
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
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 1));

    Coordinates newCoords = CommonFactory.createCoordinates(2, 2);

    assert !entity.getCoordinatesWrapper().getCoordinates().equals(newCoords);

    entity.updateAttribute(new CoordinatesWrapper(newCoords));

    assert entity.getCoordinatesWrapper().getCoordinates().equals(newCoords);
  }

  @Test
  public void testHealthUpdate() {
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 1));

    Health newHealth = new Health(22);

    assert !entity.health.equals(newHealth);

    entity.updateAttribute(newHealth);

    assert entity.health.equals(newHealth);
  }

  @Test
  public void testEquippedUpdate() {
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 1));

    Equipped newEquipped = new Equipped(3);

    assert !entity.getBag().getEquipped().equals(newEquipped);

    entity.updateAttribute(newEquipped);

    assert entity.getBag().getEquipped().equals(newEquipped);
  }

  @Test
  public void testItemUpdate() {
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 1));

    OrbInventoryItem orb1 = new OrbInventoryItem(3);

    assert entity.getBag().getItem(3) instanceof EmptyInventoryItem;

    entity.updateAttribute(orb1);

    assert entity.getBag().getItem(3) instanceof OrbInventoryItem;
  }

  @Test
  public void testAnimationStateUpdate() {
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 1));

    AnimationStateWrapper animationStateWrapper =
        new AnimationStateWrapper(AnimationState.PUNCH_LEFT);

    assert entity.getAnimationStateWrapper().getAnimationState().equals(AnimationState.DEFAULT);

    entity.updateAttribute(animationStateWrapper);

    assert entity.getAnimationStateWrapper().getAnimationState().equals(AnimationState.PUNCH_LEFT);
  }
}

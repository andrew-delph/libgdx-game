package entity.attributes.actions.comsumers;

import com.google.inject.Inject;
import entity.Entity;
import entity.attributes.actions.AttributeActionType;
import entity.attributes.msc.Coordinates;
import entity.collision.RayCastService;
import java.util.LinkedList;
import java.util.List;

public class DefaultAttributeActionConsumer implements AttributeActionConsumerInterface {

  @Inject RayCastService rayCastService;

  @Override
  public AttributeActionType getAttributeActionType() {
    return AttributeActionType.DEFAULT;
  }

  @Override
  public void use(Entity controlee) {
    Coordinates center = controlee.getCenter();

    List<Entity> hitEntityList =
        new LinkedList<>(rayCastService.rayCast(center.getLeft(), center.getRight()));

    for (Entity hitEntity : hitEntityList) {
      if (hitEntity.equals(controlee)) continue;

      if (!hitEntity.getClass().equals(Entity.class)) continue;

      //
    }
  }
}

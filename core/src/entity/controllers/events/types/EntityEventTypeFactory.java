package entity.controllers.events.types;

import entity.Entity;
import entity.attributes.Coordinates;

public class EntityEventTypeFactory {
  public FallDamageEventType createFallDamageEventType(
      Coordinates last_position, Coordinates new_position, Entity entity) {
    return new FallDamageEventType(last_position, new_position, entity);
  }
}

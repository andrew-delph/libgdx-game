package core.entity.controllers.events.types;

import core.common.events.types.EventType;
import core.entity.Entity;

public abstract class AbstractEntityEventType extends EventType {

  public static String type = "ENTITY_EVENT";
  final Entity entity;

  public AbstractEntityEventType(Entity entity) {
    this.entity = entity;
  }

  public Entity getEntity() {
    return entity;
  }

  @Override
  public String getEventType() {
    return type;
  }

  public abstract String getEntityEventType();
}

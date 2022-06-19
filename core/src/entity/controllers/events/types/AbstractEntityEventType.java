package entity.controllers.events.types;

import common.events.types.EventType;
import entity.Entity;

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

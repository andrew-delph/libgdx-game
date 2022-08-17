package core.common.events.types;

import core.entity.Entity;

public class CreateEntityEventType extends EventType {

  public static String type = "create_entity";

  Entity entity;

  public CreateEntityEventType(Entity entity) {
    this.entity = entity;
  }

  public Entity getEntity() {
    return entity;
  }

  @Override
  public String getEventType() {
    return type;
  }
}

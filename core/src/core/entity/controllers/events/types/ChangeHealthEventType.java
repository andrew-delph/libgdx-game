package core.entity.controllers.events.types;

import core.entity.Entity;

public class ChangeHealthEventType extends AbstractEntityEventType {

  public static final String type = "CHANGE_HEALTH";

  public ChangeHealthEventType(Entity entity) {
    super(entity);
  }

  @Override
  public String getEntityEventType() {
    return type;
  }
}

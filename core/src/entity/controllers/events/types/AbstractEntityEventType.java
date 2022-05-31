package entity.controllers.events.types;

import common.events.types.EventType;

public abstract class AbstractEntityEventType extends EventType {

  public static String type = "ENTITY_EVENT";

  @Override
  public String getEventType() {
    return type;
  }

  abstract String getEntityEventType();
}

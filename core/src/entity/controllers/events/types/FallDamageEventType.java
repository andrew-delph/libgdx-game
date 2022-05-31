package entity.controllers.events.types;

import entity.Entity;
import entity.attributes.Coordinates;

class FallDamageEventType extends AbstractEntityEventType {

  public static final String type = "Fall_Damage";
  Coordinates last_position;
  Coordinates new_position;
  Entity entity;

  public FallDamageEventType(Coordinates last_position, Coordinates new_position, Entity entity) {
    this.last_position = last_position;
    this.new_position = new_position;
    this.entity = entity;
  }

  @Override
  String getEntityEventType() {
    return type;
  }
}

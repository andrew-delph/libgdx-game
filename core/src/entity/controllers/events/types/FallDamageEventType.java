package entity.controllers.events.types;

import entity.Entity;
import entity.attributes.Coordinates;

public class FallDamageEventType extends AbstractEntityEventType {

  public static final String type = "Fall_Damage";
  final Coordinates last_position;
  final Coordinates new_position;

  public FallDamageEventType(Coordinates last_position, Coordinates new_position, Entity entity) {
    super(entity);
    this.last_position = last_position;
    this.new_position = new_position;
  }

  public Coordinates getLast_position() {
    return last_position;
  }

  public Coordinates getNew_position() {
    return new_position;
  }

  @Override
  public String getEntityEventType() {
    return type;
  }
}

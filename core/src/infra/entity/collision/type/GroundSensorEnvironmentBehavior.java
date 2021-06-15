package infra.entity.collision.type;

import infra.entity.Entity;

public class GroundSensorEnvironmentBehavior implements EnvironmentBehavior {
  private Entity owner;

  public GroundSensorEnvironmentBehavior(Entity owner) {
    this.owner = owner;
  }

  @Override
  public Object getSource() {
    return owner;
  }
}

package infra.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class HorizontalMovementAction implements EntityAction {

  int magnitude;

  HorizontalMovementAction(int magnitude) {
    this.magnitude = magnitude;
  }

  @Override
  public void apply(Body body) {
    body.setLinearVelocity(new Vector2(this.magnitude, body.getLinearVelocity().y));
  }
}

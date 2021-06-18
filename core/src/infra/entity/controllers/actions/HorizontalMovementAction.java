package infra.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class HorizontalMovementAction implements EntityAction {

  int magnitude;

  @Inject
  HorizontalMovementAction(@Assisted int magnitude) {
    this.magnitude = magnitude;
  }

  @Override
  public void apply(Body body) {
    body.setLinearVelocity(new Vector2(this.magnitude, body.getLinearVelocity().y));
  }

  @Override
  public Boolean isValid(Body body) {
    return true;
  }
}
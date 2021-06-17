package infra.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class JumpMovementAction implements EntityAction {
  @Override
  public void apply(Body body) {
    body.setLinearVelocity(new Vector2(0, 9));
  }
}

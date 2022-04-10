package entity.collision.ground;

import com.badlogic.gdx.physics.box2d.Body;
import entity.collision.CollisionPoint;

public class GroundPoint extends CollisionPoint {

  public GroundPoint(Body body) {
    super(body);
  }
}

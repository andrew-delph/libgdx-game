package entity.collision.ground;

import com.badlogic.gdx.physics.box2d.Body;
import entity.collision.CollisionPoint;

public class GroundSensorPoint extends CollisionPoint {

  public GroundSensorPoint(Body body) {
    super(body);
  }
}

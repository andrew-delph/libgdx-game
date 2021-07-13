package infra.entity.collision.ground;

import com.badlogic.gdx.physics.box2d.Body;

public class GroundSensorPoint {

  private final Body body;

  public GroundSensorPoint(Body body) {
    this.body = body;
  }

  public Body getBody() {
    return body;
  }
}

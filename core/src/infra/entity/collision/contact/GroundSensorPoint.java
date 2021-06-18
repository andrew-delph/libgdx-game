package infra.entity.collision.contact;

import com.badlogic.gdx.physics.box2d.Body;

public class GroundSensorPoint {

  private Body body;

  public GroundSensorPoint(Body body) {
    this.body = body;
  }

  public Body getBody() {
    return body;
  }
}

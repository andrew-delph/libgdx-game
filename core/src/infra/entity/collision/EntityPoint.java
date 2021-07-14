package infra.entity.collision;

import com.badlogic.gdx.physics.box2d.Body;

public class EntityPoint {
  Body body;

  public EntityPoint(Body body) {
    this.body = body;
  }

  public Body getBody() {
    return body;
  }
}

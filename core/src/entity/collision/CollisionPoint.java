package entity.collision;

import com.badlogic.gdx.physics.box2d.Body;
import chunk.world.exceptions.BodyNotFound;

public abstract class CollisionPoint {

  private final Body body;

  public CollisionPoint(Body body) {
    this.body = body;
  }

  public Body getBody() throws BodyNotFound {
    if (body == null) throw new BodyNotFound("In CollisionPoint");
    return body;
  }
}

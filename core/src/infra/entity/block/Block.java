package infra.entity.block;

import com.badlogic.gdx.physics.box2d.*;
import infra.entity.Entity;
import infra.entity.collision.contact.GroundPoint;

public abstract class Block extends Entity {

  public Block() {
    super();
    this.textureName = "badlogic.jpg";
    this.zindex = 0;
  }

  @Override
  public Body getBody() {
    return this.body;
  }

  @Override
  public void setBody(Body body) {
    this.body = body;
  }

  public abstract Body addWorld(World world);
}

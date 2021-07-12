package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import infra.entity.Entity;

public abstract class Block extends Entity {

  public static int staticHeight = Entity.coordinatesScale;
  public static int staticWidth = Entity.coordinatesScale;

  public Block() {
    super();
    this.textureName = "badlogic.jpg";
    this.zindex = 0;
    this.setHeight(Block.staticHeight);
    this.setWidth(Block.staticWidth);
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

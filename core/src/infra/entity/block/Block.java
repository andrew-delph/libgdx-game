package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;
import infra.entity.Entity;
import infra.entity.EntityBodyBuilder;

public abstract class Block extends Entity {

  public static int staticHeight = Entity.coordinatesScale;
  public static int staticWidth = Entity.coordinatesScale;

  public Block(Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
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

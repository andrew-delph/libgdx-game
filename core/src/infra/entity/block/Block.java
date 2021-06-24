package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;
import infra.entity.Entity;

public class Block extends Entity {

  public Block(Clock clock, BaseAssetManager baseAssetManager) {
    super(clock, baseAssetManager);
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

  public Body addWorld(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        this.coordinates.getXReal() * Entity.coordinatesScale,
        this.coordinates.getYReal() * Entity.coordinatesScale);

    //    this.setBody(world.createBody(bodyDef));
    Body theBody = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(Entity.coordinatesScale / 2.0f, Entity.coordinatesScale / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.1f;
    fixtureDef.restitution = 0.5f;
    theBody.createFixture(fixtureDef);
    return theBody;
  }
}

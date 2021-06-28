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
    fixtureDef.density = 0f;
    fixtureDef.restitution = 0;
    Fixture blockFixture = theBody.createFixture(fixtureDef);
    blockFixture.setUserData(new GroundPoint());
    return theBody;
  }
}

package infra.entity.block;

import com.badlogic.gdx.physics.box2d.*;
import infra.entity.Entity;

public class Block extends Entity {

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

  public void addWorld(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        this.coordinates.getXReal() * Entity.coordinatesScale,
        this.coordinates.getYReal() * Entity.coordinatesScale);

    this.setBody(world.createBody(bodyDef));

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(Entity.coordinatesScale / 2.0f, Entity.coordinatesScale / 2.0f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.1f;
    fixtureDef.restitution = 0.5f;
    this.getBody().createFixture(fixtureDef);
  }
}

package infra.entity.block;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import infra.entity.Entity;

public class Block extends Entity {
  public Block() {
    super();
    this.textureName = "badlogic.jpg";
    this.zindex = 0;
  }

  public synchronized void addWorld(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        this.coordinates.getXReal() * Entity.coordinatesScale,
        this.coordinates.getYReal() * Entity.coordinatesScale);

    body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.1f;
    fixtureDef.restitution = 0.5f;
    body.createFixture(fixtureDef);
  }
}

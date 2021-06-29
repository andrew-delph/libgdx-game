package infra.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.entity.collision.contact.GroundPoint;
import infra.entity.collision.contact.GroundSensorPoint;

public class EntityBodyBuilder {

  @Inject
  public EntityBodyBuilder() {}

  public Body createEntityBody(World world, Coordinates coordinates) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(
        coordinates.getXReal() * Entity.coordinatesScale,
        coordinates.getYReal() * Entity.coordinatesScale);

    Body theBody = world.createBody(bodyDef);

    theBody.setUserData(this);

    PolygonShape shape = new PolygonShape();

    shape.setAsBox(Entity.coordinatesScale / 2f, Entity.coordinatesScale / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    fixtureDef.restitution = 0;
    theBody.createFixture(fixtureDef);

    FixtureDef jumpFixtureDef = new FixtureDef();
    PolygonShape jumpShape = new PolygonShape();
    jumpShape.setAsBox(
        Entity.coordinatesScale / 2f, 1f, new Vector2(0, -Entity.coordinatesScale / 2f), 0);
    jumpFixtureDef.shape = jumpShape;
    jumpFixtureDef.isSensor = true;

    Fixture jumpFixture = theBody.createFixture(jumpFixtureDef);
    jumpFixture.setUserData(new GroundSensorPoint(theBody));
    theBody.setFixedRotation(true);
    return theBody;
  }

  public Body createSolidBlockBody(World world, Coordinates coordinates) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        coordinates.getXReal() * Entity.coordinatesScale,
        coordinates.getYReal() * Entity.coordinatesScale);

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

  public Body createEmptyBlockBody() {
    return null;
  }
}

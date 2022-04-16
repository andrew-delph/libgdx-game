package chunk.world;

import chunk.ChunkRange;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import com.sun.tools.javac.util.Pair;
import common.GameSettings;
import entity.Entity;
import entity.block.Block;
import entity.collision.EntityPoint;
import entity.collision.ground.GroundPoint;
import entity.collision.ground.GroundSensorPoint;
import entity.collision.ladder.LadderPoint;
import entity.collision.left.LeftSensorPoint;
import entity.collision.right.RightSensorPoint;
import java.util.UUID;

public class EntityBodyBuilder {

  @Inject
  public EntityBodyBuilder() {}

  public static Pair<UUID, Body> createEntityBody(
      World world, ChunkRange chunkRange, Entity entity) {
    float center_x =
        -(GameSettings.PHYSICS_SCALE - (Entity.staticWidth * GameSettings.PHYSICS_SCALE)) / 2f;
    float center_y =
        -(GameSettings.PHYSICS_SCALE - (Entity.staticHeight * GameSettings.PHYSICS_SCALE)) / 2f;

    Body theBody;
    BodyDef bodyDef = new BodyDef();

    FixtureDef mainFixtureDef = new FixtureDef();
    PolygonShape mainShape = new PolygonShape();
    Fixture mainFixture;

    FixtureDef smoothFixtureDef = new FixtureDef();
    PolygonShape smoothShape = new PolygonShape();
    Fixture smoothFixture;

    FixtureDef jumpFixtureDef = new FixtureDef();
    PolygonShape jumpShape = new PolygonShape();
    Fixture jumpFixture;

    FixtureDef leftFixtureDef = new FixtureDef();
    PolygonShape leftShape = new PolygonShape();
    Fixture leftFixture;

    FixtureDef rightFixtureDef = new FixtureDef();
    PolygonShape rightShape = new PolygonShape();
    Fixture rightFixture;

    Filter filter = new Filter();
    filter.categoryBits = 1;
    filter.maskBits = 2;

    // create the body
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(
        entity.coordinates.getXReal() * GameSettings.PHYSICS_SCALE,
        entity.coordinates.getYReal() * GameSettings.PHYSICS_SCALE);
    theBody = world.createBody(bodyDef);
    theBody.setFixedRotation(true);

    // create the main
    mainShape.setAsBox(
        (Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f,
        (Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f,
        new Vector2(center_x, center_y + 2),
        0);
    mainFixtureDef.shape = mainShape;
    mainFixtureDef.density = 1f;
    mainFixtureDef.restitution = 0;
    mainFixture = theBody.createFixture(mainFixtureDef);
    mainFixture.setFilterData(filter);
    mainFixture.setUserData(new EntityPoint(entity.uuid, chunkRange));

    // create the smooth
    Vector2[] smoothVertices = new Vector2[3];
    float left = center_x - ((Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f) - 1;
    float right = center_x + ((Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f) + 1;
    float bottom = center_y - ((Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f) - 1;
    smoothVertices[0] = new Vector2(left, center_y);
    smoothVertices[1] = new Vector2(right, center_y);
    smoothVertices[2] = new Vector2(center_x, bottom);
    smoothShape.set(smoothVertices);
    smoothFixtureDef.shape = smoothShape;
    smoothFixtureDef.density = 1f;
    smoothFixtureDef.restitution = 0;
    smoothFixture = theBody.createFixture(smoothFixtureDef);
    smoothFixture.setFilterData(filter);

    // create the jump
    jumpShape.setAsBox(
        (Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f,
        5f,
        new Vector2(
            -(Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 8f,
            -(Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f - 3f),
        0);
    jumpFixtureDef.shape = jumpShape;
    jumpFixtureDef.isSensor = true;
    jumpFixture = theBody.createFixture(jumpFixtureDef);
    jumpFixture.setUserData(new GroundSensorPoint(entity.getUuid(), chunkRange));
    jumpFixture.setFilterData(filter);

    // create the left
    leftShape.setAsBox(
        5f,
        (Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f,
        new Vector2(-(Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f, 0),
        0);
    leftFixtureDef.shape = leftShape;
    leftFixtureDef.isSensor = true;
    leftFixture = theBody.createFixture(leftFixtureDef);
    leftFixture.setUserData(new LeftSensorPoint(entity.uuid, chunkRange));
    leftFixture.setFilterData(filter);

    // create the right
    rightShape.setAsBox(
        5f,
        (Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f,
        new Vector2(((Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f) - 5, 0),
        0);
    rightFixtureDef.shape = rightShape;
    rightFixtureDef.isSensor = true;
    rightFixture = theBody.createFixture(rightFixtureDef);
    rightFixture.setUserData(new RightSensorPoint(entity.uuid, chunkRange));
    rightFixture.setFilterData(filter);

    return new Pair<>(entity.uuid, theBody);
  }

  public static Pair<UUID, Body> createSolidBlockBody(
      World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        entity.coordinates.getXReal() * GameSettings.PHYSICS_SCALE,
        entity.coordinates.getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(
        (Block.staticWidth * GameSettings.PHYSICS_SCALE) / 2f,
        (Block.staticHeight * GameSettings.PHYSICS_SCALE) / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0f;
    fixtureDef.restitution = 0;
    Fixture blockFixture = theBody.createFixture(fixtureDef);

    Filter filter = new Filter();
    filter.categoryBits = 2;
    filter.maskBits = 1;
    blockFixture.setFilterData(filter);

    blockFixture.setUserData(new GroundPoint(entity.uuid, chunkRange));
    return new Pair<>(entity.uuid, theBody);
  }

  public static Pair<UUID, Body> createEmptyBlockBody(
      World world, ChunkRange chunkRange, Entity entity) {
    return null;
  }

  public static Pair<UUID, Body> createEmptyLadderBody(
      World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        entity.coordinates.getXReal() * GameSettings.PHYSICS_SCALE,
        entity.coordinates.getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(
        (Block.staticWidth * GameSettings.PHYSICS_SCALE) / 2.0f,
        (Block.staticHeight * GameSettings.PHYSICS_SCALE) / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0f;
    fixtureDef.restitution = 0;
    fixtureDef.isSensor = true;
    Fixture blockFixture = theBody.createFixture(fixtureDef);

    Filter filter = new Filter();
    filter.categoryBits = 2;
    filter.maskBits = 1;

    blockFixture.setFilterData(filter);
    blockFixture.setUserData(new LadderPoint(entity.uuid, chunkRange));

    return new Pair<>(entity.uuid, theBody);
  }
}

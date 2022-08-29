package core.chunk.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import core.common.ChunkRange;
import core.common.GameSettings;
import core.entity.Entity;
import core.entity.block.Block;
import core.entity.collision.EntitySensor;
import core.entity.collision.ground.EntityFeetSensor;
import core.entity.collision.ground.GroundSensor;
import core.entity.collision.ladder.LadderSensor;
import core.entity.collision.left.LeftSensor;
import core.entity.collision.orb.OrbSensor;
import core.entity.collision.projectile.ProjectileSensor;
import core.entity.collision.right.RightSensor;
import core.entity.misc.Projectile;
import core.entity.misc.water.WaterPosition;
import java.util.UUID;

public class EntityBodyBuilder {

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

    // create the body
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);
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
    mainFixture.setFilterData(entityFilter());
    mainFixture.setUserData(new EntitySensor(entity, chunkRange));

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
    smoothFixture.setFilterData(entityFilter());

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
    jumpFixture.setUserData(new EntityFeetSensor(entity, chunkRange));
    jumpFixture.setFilterData(entityFilter());

    // create the left
    leftShape.setAsBox(
        5f,
        (Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f,
        new Vector2(-(Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f, 0),
        0);
    leftFixtureDef.shape = leftShape;
    leftFixtureDef.isSensor = true;
    leftFixture = theBody.createFixture(leftFixtureDef);
    leftFixture.setUserData(new LeftSensor(entity, chunkRange));
    leftFixture.setFilterData(entityFilter());

    // create the right
    rightShape.setAsBox(
        5f,
        (Entity.staticHeight * GameSettings.PHYSICS_SCALE) / 2f,
        new Vector2(((Entity.staticWidth * GameSettings.PHYSICS_SCALE) / 2f) - 5, 0),
        0);
    rightFixtureDef.shape = rightShape;
    rightFixtureDef.isSensor = true;
    rightFixture = theBody.createFixture(rightFixtureDef);
    rightFixture.setUserData(new RightSensor(entity, chunkRange));
    rightFixture.setFilterData(entityFilter());

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Pair<UUID, Body> createSolidBlockBody(
      World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(
        (Block.staticWidth * GameSettings.PHYSICS_SCALE) / 2f,
        (Block.staticHeight * GameSettings.PHYSICS_SCALE) / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 500f;
    fixtureDef.restitution = 0;
    fixtureDef.friction = 0.1f;
    Fixture blockFixture = theBody.createFixture(fixtureDef);

    blockFixture.setFilterData(blockFilter());
    blockFixture.setUserData(new GroundSensor(entity, chunkRange));

    //    theBody.resetMassData();

    return new Pair<>(entity.getUuid(), theBody);
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
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

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

    blockFixture.setFilterData(ladderFilter());
    blockFixture.setUserData(new LadderSensor(entity, chunkRange));

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Pair<UUID, Body> createProjectileBody(
      World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(
        (Projectile.staticWidth * GameSettings.PHYSICS_SCALE) / 2.0f,
        (Projectile.staticHeight * GameSettings.PHYSICS_SCALE) / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    fixtureDef.restitution = 0;
    fixtureDef.isSensor = true;
    Fixture projectileFixture = theBody.createFixture(fixtureDef);

    projectileFixture.setFilterData(projectileFilter());
    projectileFixture.setUserData(new ProjectileSensor(entity, chunkRange));

    theBody.setGravityScale(0);

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Pair<UUID, Body> createTurretBody(
      World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

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

    blockFixture.setFilterData(ladderFilter());
    //    blockFixture.setUserData(new LadderSensor(entity, chunkRange));

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Pair<UUID, Body> createOrb(World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);
    theBody.setFixedRotation(true);

    PolygonShape blockingShape = new PolygonShape();
    blockingShape.setAsBox(
        (Block.staticWidth * GameSettings.PHYSICS_SCALE) / 2.1f,
        (Block.staticHeight * GameSettings.PHYSICS_SCALE) / 2.1f);
    FixtureDef blockingFixtureDef = new FixtureDef();
    blockingFixtureDef.shape = blockingShape;
    blockingFixtureDef.density = 1f;
    blockingFixtureDef.restitution = 1;
    //    fixtureDef.isSensor = true;
    Fixture orbFixture = theBody.createFixture(blockingFixtureDef);
    orbFixture.setFilterData(entityFilter());

    PolygonShape sensorShape = new PolygonShape();
    sensorShape.setAsBox(
        (Block.staticWidth * GameSettings.PHYSICS_SCALE) / 2f,
        (Block.staticHeight * GameSettings.PHYSICS_SCALE) / 2f);

    FixtureDef sensorFixtureDef = new FixtureDef();
    sensorFixtureDef.shape = sensorShape;
    sensorFixtureDef.density = 1f;
    sensorFixtureDef.restitution = 1;
    sensorFixtureDef.isSensor = true;

    Fixture sensorFixture = theBody.createFixture(sensorFixtureDef);
    sensorFixture.setUserData(new OrbSensor(entity, chunkRange));
    sensorFixture.setFilterData(projectileFilter());

    theBody.setLinearDamping(0.1f);
    theBody.setAngularDamping(0.1f);

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Pair<UUID, Body> createWaterPosition(
      World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);

    CircleShape circleShape = new CircleShape();
    circleShape.setRadius((WaterPosition.staticHeight * GameSettings.PHYSICS_SCALE) / 2f);

    FixtureDef blockingFixtureDef = new FixtureDef();
    blockingFixtureDef.shape = circleShape;
    //    blockingFixtureDef.density = 500f;
    blockingFixtureDef.friction = 0.1f;
    blockingFixtureDef.restitution = 0;

    Fixture orbFixture = theBody.createFixture(blockingFixtureDef);
    orbFixture.setFilterData(waterPositionFilter());

    //    theBody.setGravityScale(0.1f);
    //    theBody.resetMassData();

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Pair<UUID, Body> createWater(World world, ChunkRange chunkRange, Entity entity) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.StaticBody;
    bodyDef.position.set(
        entity.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PHYSICS_SCALE,
        entity.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PHYSICS_SCALE);

    Body theBody = world.createBody(bodyDef);
    theBody.setFixedRotation(true);

    PolygonShape blockingShape = new PolygonShape();
    blockingShape.setAsBox(
        (Block.staticWidth * GameSettings.PHYSICS_SCALE) / 2.1f,
        (Block.staticHeight * GameSettings.PHYSICS_SCALE) / 2.1f);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = blockingShape;
    fixtureDef.density = 1f;
    fixtureDef.restitution = 0;
    fixtureDef.isSensor = true;
    Fixture projectileFixture = theBody.createFixture(fixtureDef);

    projectileFixture.setFilterData(ladderFilter());
    projectileFixture.setUserData(new LadderSensor(entity, chunkRange));

    return new Pair<>(entity.getUuid(), theBody);
  }

  public static Filter entityFilter() {
    // entity collides with blocks
    Filter filter = new Filter();
    filter.categoryBits = 0b1;
    filter.maskBits = 0b1110;
    return filter;
  }

  public static Filter blockFilter() {
    // blocks collide with everything
    Filter filter = new Filter();
    filter.categoryBits = 0b10;
    filter.maskBits = 0b1111;
    return filter;
  }

  public static Filter ladderFilter() {
    // ladder collides with entities
    Filter filter = new Filter();
    filter.categoryBits = 0b10;
    filter.maskBits = 0b1;
    return filter;
  }

  public static Filter projectileFilter() {
    // collides with entities and blocks
    Filter filter = new Filter();
    filter.categoryBits = 0b100;
    filter.maskBits = 0b11;
    return filter;
  }

  public static Filter waterPositionFilter() {
    // collides with blocks and waterPosition
    Filter filter = new Filter();
    filter.categoryBits = 0b100;
    filter.maskBits = 0b110;
    return filter;
  }
}

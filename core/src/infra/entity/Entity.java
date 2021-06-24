package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.UUID;

import infra.common.Clock;
import infra.common.Coordinates;
import infra.common.render.BaseAssetManager;
import infra.entity.controllers.EntityController;
import infra.networking.NetworkObjects;

public class Entity {
  public static int coordinatesScale = 25;
  public UUID uuid;
  public EntityController entityController;
  public Animation animation;
  public Sprite sprite;
  public Body body;
  public Coordinates coordinates;
  public int zindex = 1;
  public int width = (int) (coordinatesScale);
  public int height = (int) (coordinatesScale);
  public String textureName = "frog.png";

  Clock clock;
  BaseAssetManager baseAssetManager;

  public Entity(Clock clock, BaseAssetManager baseAssetManager) {
    this.clock = clock;
    this.baseAssetManager = baseAssetManager;
    this.sprite = new Sprite();
    this.sprite.setPosition(0, 0);
    this.sprite.setSize(width, height);
    this.coordinates = new Coordinates(0, 0);
    this.uuid = UUID.randomUUID();
    this.entityController = new EntityController(null,this);
  }

  public Body getBody() {
    //    System.out.println("getBody");
    return body;
  }

  public void setBody(Body body) {
    //    System.out.println("setBody"+new ChunkRange(this.coordinates)+","+this.uuid);
    this.body = body;
  }

  public synchronized Body addWorld(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(
        this.coordinates.getXReal() * coordinatesScale,
        this.coordinates.getYReal() * coordinatesScale);

    //    this.setBody(world.createBody(bodyDef));
    Body theBody = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();

    shape.setAsBox(coordinatesScale / 2f, coordinatesScale / 2f);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.1f;
    fixtureDef.restitution = 0.5f;
    theBody.createFixture(fixtureDef);

    theBody.setFixedRotation(true);

    return theBody;
  }

  public synchronized void setController(EntityController entityController) {
    this.entityController = entityController;
  }

  public synchronized void renderSync() {
    this.sprite = new Sprite((Texture) baseAssetManager.get(this.textureName));
    this.sprite.setSize(width, height);
    this.sprite.setPosition(
        this.coordinates.getXReal() * coordinatesScale,
        this.coordinates.getYReal() * coordinatesScale);
  }

  public void syncPosition() {}

  public synchronized void setZindex(int zindex) {
    this.zindex = zindex;
  }

  public synchronized int getUpdateTimeout() {
    return this.clock.currentTick.time + 1;
  }

  public NetworkObjects.NetworkData toNetworkData() {
    NetworkObjects.NetworkData x =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("x")
            .setValue(String.valueOf(this.coordinates.getXReal()))
            .build();
    NetworkObjects.NetworkData y =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("y")
            .setValue(String.valueOf(this.coordinates.getYReal()))
            .build();
    NetworkObjects.NetworkData coordinates =
        NetworkObjects.NetworkData.newBuilder()
            .setKey(Coordinates.class.getName())
            .addChildren(x)
            .addChildren(y)
            .build();
    NetworkObjects.NetworkData uuid =
        NetworkObjects.NetworkData.newBuilder()
            .setKey(UUID.class.getName())
            .setValue(this.uuid.toString())
            .build();
    return NetworkObjects.NetworkData.newBuilder()
        .setKey("class")
        .setValue(this.getClass().getName())
        .addChildren(coordinates)
        .addChildren(uuid)
        .build();
  }

  @Override
  public int hashCode() {
    return (this.uuid).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Entity other = (Entity) obj;
    return this.uuid == other.uuid;
  }

  public Coordinates getCenter() {
    return new Coordinates(this.coordinates.getXReal() + 0.5f, this.coordinates.getYReal() + 0.5f);
  }
}

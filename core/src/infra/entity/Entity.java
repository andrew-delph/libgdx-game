package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.inject.Inject;
import infra.common.Clock;
import infra.common.Coordinates;
import infra.common.render.BaseAssetManager;
import infra.entity.controllers.EntityController;
import infra.networking.NetworkObjects;

import java.util.UUID;

public class Entity {
  public static int coordinatesScale = 25;
  public UUID uuid;
  public EntityController entityController;
  public Animation animation;
  public Sprite sprite;
  public Body body;
  public Coordinates coordinates;
  @Inject public Clock clock;
  public int zindex = 1;
  public int width = (int) (coordinatesScale);
  public int height = (int) (coordinatesScale);
  public String textureName = "frog.png";
  @Inject BaseAssetManager baseAssetManager;
  private int groundContact = 0;

  @Inject
  protected Entity() {
    this.sprite = new Sprite();
    this.sprite.setPosition(0, 0);
    this.sprite.setSize(width, height);
    this.coordinates = new Coordinates(0, 0);
    this.uuid = UUID.randomUUID();
    this.entityController = new EntityController(this);
  }

  public void increaseGroundContact() {
    this.groundContact++;
  }

  public void decreaseGroundContact() {
    this.groundContact--;
  }

  public Boolean isOnGround() {
    if (this.groundContact > 0) {
      return true;
    } else {
      return false;
    }
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
    fixtureDef.density = 1f;
    fixtureDef.restitution = 0;
    theBody.createFixture(fixtureDef);

    FixtureDef jumpFixtureDef = new FixtureDef();
    PolygonShape jumpShape = new PolygonShape();
    jumpShape.setAsBox(coordinatesScale / 2f, 1f, new Vector2(0, -coordinatesScale / 2f), 0);
    jumpFixtureDef.shape = jumpShape;
    jumpFixtureDef.isSensor = true;

    Fixture jumpFixture = theBody.createFixture(jumpFixtureDef);
    jumpFixture.setUserData(this);
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

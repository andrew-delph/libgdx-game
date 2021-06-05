package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
  public String textureName = "frog.png";

  @Inject BaseAssetManager baseAssetManager;

  public void setBody(Body body) {
    this.body = body;
  }

  @Inject
  public Entity() {
    this.sprite = new Sprite();
    this.sprite.setPosition(0, 0);
    this.sprite.setSize(50, 50);
    this.coordinates = new Coordinates(0, 3);
    this.uuid = UUID.randomUUID();
    this.entityController = new EntityController(this);
  }

  public synchronized void addWorld(World world) {
    try{
      System.out.println("add world");
      BodyDef bodyDef = new BodyDef();
      System.out.println(1);
      bodyDef.type = BodyDef.BodyType.StaticBody;
      System.out.println(2);
      bodyDef.position.set(this.coordinates.getXReal(), this.coordinates.getYReal());

      System.out.println(3);

      int before = world.getBodyCount();
      System.out.println(4);
      body = world.createBody(bodyDef);

      System.out.println(5);

      System.out.println("difference: "+(world.getBodyCount()-before));

      PolygonShape shape = new PolygonShape();

      System.out.println(6);
      shape.setAsBox(this.coordinatesScale, this.coordinatesScale);
      System.out.println(7);
      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.shape = shape;
      fixtureDef.density = 0.1f;
      fixtureDef.restitution = 0.5f;
      System.out.println(8);
      body.createFixture(fixtureDef);
      System.out.println(9);
      System.out.println("done");
    }catch (Exception e){
      e.printStackTrace();
    }finally{
      System.out.println("olay");
    }
  }

  public synchronized void setController(EntityController entityController) {
    this.entityController = entityController;
  }

  public synchronized void renderSync() {
    this.sprite = new Sprite((Texture) baseAssetManager.get(this.textureName));
    this.sprite.setSize(this.coordinatesScale, this.coordinatesScale);
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
}

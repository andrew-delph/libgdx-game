package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import infra.common.Clock;
import infra.common.Coordinates;
import infra.common.render.BaseAssetManager;
import infra.entity.controllers.EntityController;
import infra.networking.NetworkObjects;

import java.util.UUID;

public class Entity {
  public UUID uuid;
  public EntityController entityController;
  public Animation animation;
  public Sprite sprite;
  public Body body;
  public Coordinates coordinates;
  @Inject public Clock clock;
  public int zindex = 1;
  public static int coordinatesScale = 25;
  public String textureName = "frog.png";

  @Inject BaseAssetManager baseAssetManager;

  @Inject
  public Entity() {
    this.sprite = new Sprite();
    this.sprite.setPosition(0, 0);
    this.sprite.setSize(50, 50);
    this.coordinates = new Coordinates(0, 0);
    this.uuid = UUID.randomUUID();
    this.entityController = new EntityController(this);
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

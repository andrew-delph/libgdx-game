package entity;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.Coordinates;
import common.GameSettings;
import chunk.world.exceptions.BodyNotFound;
import entity.controllers.EntityController;
import java.util.UUID;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkData;
import networking.translation.NetworkDataSerializer;

public class Entity implements SerializeNetworkData {
  public static float staticHeight = 0.8f;
  public static float staticWidth = 0.8f;
  public UUID uuid;
  public Animation animation;
  public Sprite sprite;
  public Coordinates coordinates;
  public int zindex = 3;
  public EntityBodyBuilder entityBodyBuilder;
  Clock clock;
  BaseAssetManager baseAssetManager;
  private EntityController entityController;
  private Body body;
  private int width;
  private int height;

  public Entity(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    this.setHeight((int) (Entity.staticHeight * GameSettings.PIXEL_SCALE));
    this.setWidth((int) (Entity.staticWidth * GameSettings.PIXEL_SCALE));
    this.clock = clock;
    this.baseAssetManager = baseAssetManager;
    this.entityBodyBuilder = entityBodyBuilder;
    this.coordinates = coordinates;
    this.uuid = UUID.randomUUID();
  }

  public EntityController getEntityController() {
    return entityController;
  }

  public synchronized void setEntityController(EntityController entityController) {
    this.entityController = entityController;
  }

  public String getTextureName() {
    return "frog.png";
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public Body getBody() throws BodyNotFound {
    if (body == null) throw new BodyNotFound(this.toString());
    return body;
  }

  public void setBody(Body body) throws BodyNotFound {
    if (body == null) throw new BodyNotFound(this.toString());
    this.body = body;
  }

  public synchronized Body addWorld(World world) {
    return EntityBodyBuilder.createEntityBody(world, this.coordinates);
  }

  public synchronized void renderSync() {
    if (this.sprite == null) {
      this.sprite = new Sprite((Texture) baseAssetManager.get(this.getTextureName()));
      this.sprite.setSize(this.getWidth(), this.getHeight());
    }
    this.sprite.setPosition(
        this.coordinates.getXReal() * GameSettings.PIXEL_SCALE,
        this.coordinates.getYReal() * GameSettings.PIXEL_SCALE);
  }

  public void syncPosition() {}

  public synchronized void setZindex(int zindex) {
    this.zindex = zindex;
  }

  public synchronized int getUpdateTimeout() {
    return 1;
  }

  public NetworkObjects.NetworkData toNetworkData() {
    return NetworkDataSerializer.createEntity(this);
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
    return this.uuid.equals(other.uuid) && this.coordinates.equals(other.coordinates);
  }

  public Coordinates getCenter() {
    return new Coordinates(this.coordinates.getXReal() + 0.5f, this.coordinates.getYReal() + 0.5f);
  }

  @Override
  public String toString() {
    return "Entity{" + "uuid=" + uuid + ", coordinates=" + coordinates + ", body=" + body + '}';
  }

  public UUID getUuid() {
    return this.uuid;
  }
}

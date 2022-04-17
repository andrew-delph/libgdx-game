package entity;

import app.screen.BaseAssetManager;
import chunk.Chunk;
import chunk.world.CreateBodyCallable;
import chunk.world.EntityBodyBuilder;
import chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import common.Clock;
import common.Coordinates;
import common.GameSettings;
import common.exceptions.ChunkNotFound;
import entity.controllers.EntityController;
import java.util.UUID;
import java.util.function.Consumer;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkData;
import networking.translation.NetworkDataSerializer;

public class Entity implements SerializeNetworkData {
  public static float staticHeight = 0.8f;
  public static float staticWidth = 0.8f;
  private final Clock clock;
  public Animation animation;
  public Coordinates coordinates;
  public int zindex = 3;
  public EntityBodyBuilder entityBodyBuilder;
  public Sprite sprite;
  BaseAssetManager baseAssetManager;
  private UUID uuid;
  private Chunk chunk;
  private EntityController entityController;
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

  public Chunk getChunk() throws ChunkNotFound {
    if (chunk == null) throw new ChunkNotFound(this.toString());
    return chunk;
  }

  public void setChunk(Chunk chunk) {
    this.chunk = chunk;
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

  public CreateBodyCallable addWorld(Chunk chunk) {
    Entity myEntity = this;
    return new CreateBodyCallable() {
      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createEntityBody(
            world, chunk.chunkRange, myEntity); // TODO test with Entity.this
      }
    };
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

  public synchronized void setZindex(int zindex) {
    this.zindex = zindex;
  }

  public int getUpdateTimeout() {
    return 1;
  }

  public NetworkObjects.NetworkData toNetworkData() {
    return NetworkDataSerializer.createEntity(this);
  }

  public Vector2 getBodyVelocity() throws BodyNotFound, ChunkNotFound {
    return getChunk().getWorldWrapper().getVelocity(this);
  }

  public void setBodyVelocity(Vector2 velocity) throws ChunkNotFound, BodyNotFound {
    getChunk().getWorldWrapper().setVelocity(this, velocity);
  }

  public Vector2 getBodyPosition() throws BodyNotFound, ChunkNotFound {
    return getChunk().getWorldWrapper().getPosition(this);
  }

  public void setBodyPosition(Vector2 position) throws ChunkNotFound, BodyNotFound {
    getChunk().getWorldWrapper().setPosition(this, position);
  }

  public void applyBody(Consumer<Body> applyFunction) throws ChunkNotFound, BodyNotFound {
    getChunk().getWorldWrapper().applyBody(this, applyFunction);
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
    return "Entity{" + "uuid=" + uuid + ", coordinates=" + coordinates + '}';
  }

  public UUID getUuid() {
    return this.uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }
}

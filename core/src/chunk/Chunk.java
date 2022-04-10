package chunk;

import static common.GameSettings.GRAVITY;

import app.GameController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.Coordinates;
import common.GameSettings;
import common.GameStore;
import common.Tick;
import common.exceptions.BodyNotFound;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.block.Block;
import entity.collision.EntityContactListenerFactory;
import entity.misc.Ladder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk implements Callable<Chunk>, SerializeNetworkData {

  final Logger LOGGER = LogManager.getLogger();
  private final WorldWrapper worldWrapper;
  public ChunkRange chunkRange;
  public Tick updateTick;
  public World world;
  GameStore gameStore;
  GameController gameController;
  Clock clock;
  ConcurrentHashMap<UUID, Entity> chunkMap;
  Set<UUID> bodySet;
  Map<Entity, Body> neighborEntityBodyMap = new HashMap<>();

  public Chunk(
      Clock clock,
      GameStore gameStore,
      GameController gameController,
      EntityContactListenerFactory entityContactListenerFactory,
      ChunkRange chunkRange) {
    this.gameStore = gameStore;
    this.gameController = gameController;
    this.clock = clock;
    this.chunkRange = chunkRange;
    this.chunkMap = new ConcurrentHashMap<>();
    this.nextTick(1);
    this.bodySet = new HashSet<>();
    World world = new World(new Vector2(0, -GRAVITY), false);
    world.setContactListener(entityContactListenerFactory.createEntityContactListener());
    this.worldWrapper = new WorldWrapper(world);
  }

  void nextTick(int timeout) {
    this.updateTick = new Tick(clock.getCurrentTick().time + timeout);
  }

  @Override
  public Chunk call() throws Exception {
    try {
      this.update();
    } catch (Exception e) {
      LOGGER.error("CHUNK UPDATE FAILED", e);
    }
    return this;
  }

  public synchronized Entity removeEntity(UUID uuid) throws EntityNotFound {
    Entity entity = this.getEntity(uuid);
    this.chunkMap.remove(uuid);
    if (bodySet.contains(entity.uuid)) {
      try {
        this.world.destroyBody(entity.getBody());
        bodySet.remove(entity.uuid);
      } catch (Exception e) {
        LOGGER.error("Unable to delete body for:" + entity.uuid, e);
      }
    }
    return entity;
  }

  public synchronized void addEntity(Entity entity) {
    this.chunkMap.put(entity.uuid, entity);
    if (!bodySet.contains(entity.uuid)) {
      Body bodyToAdd = entity.addWorld(world);
      if (bodyToAdd != null) {
        try {
          entity.setBody(bodyToAdd);
          bodySet.add(entity.uuid);
        } catch (BodyNotFound e) {
          LOGGER.error(e, e);
        }
      }
    }
    this.nextTick(1);
  }

  public void addAllEntity(List<Entity> entityList) {
    for (Entity entity : entityList) {
      this.addEntity(entity);
    }
  }

  public Entity getEntity(UUID uuid) throws EntityNotFound {
    Entity toReturn = this.chunkMap.get(uuid);
    if (toReturn == null) throw new EntityNotFound("Entity not found in chunk #UUID " + uuid);
    return this.chunkMap.get(uuid);
  }

  public Set<UUID> getEntityUUIDSet() {
    return this.chunkMap.keySet();
  }

  public List<Entity> getEntityList() {
    return new LinkedList<Entity>(this.chunkMap.values());
  }

  public List<Chunk> getNeighborChunks() {
    List<Chunk> neighborChunkList = new LinkedList<>();
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getUp()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getDown()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getLeft()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getRight()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getLeft().getUp()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getLeft().getDown()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getRight().getUp()));
    neighborChunkList.add(this.gameStore.getChunk(this.chunkRange.getRight().getDown()));
    neighborChunkList.removeIf(Objects::isNull);
    return neighborChunkList;
  }

  private void updateNeighbors() {
    Set<Entity> neighborEntitySet = new HashSet<>();

    Coordinates neighborBottomLeft =
        (new Coordinates(this.chunkRange.bottom_x, this.chunkRange.bottom_y))
            .getLeft()
            .getLeft()
            .getDown()
            .getDown();
    Coordinates neighborTopRight =
        (new Coordinates(this.chunkRange.top_x, this.chunkRange.top_y))
            .getRight()
            .getRight()
            .getUp()
            .getUp();

    for (Chunk neighbor : getNeighborChunks()) {
      if (neighbor == null) continue;
      neighborEntitySet.addAll(neighbor.getEntityInRange(neighborBottomLeft, neighborTopRight));
    }

    // check the difference
    Set<Entity> entityToAddSet = new HashSet<>(neighborEntitySet);
    entityToAddSet.removeAll(neighborEntityBodyMap.keySet());
    Set<Entity> entityToRemoveSet = new HashSet<>(neighborEntityBodyMap.keySet());
    entityToRemoveSet.removeAll(neighborEntitySet);

    // add temp entity to set
    for (Entity entity : entityToAddSet) {
      if (neighborEntityBodyMap.containsKey(entity)) continue;

      Body bodyToAdd = entity.addWorld(world);
      if (bodyToAdd == null) continue;
      neighborEntityBodyMap.put(entity, bodyToAdd);
    }

    // remove temp entity from set
    for (Entity entity : entityToRemoveSet) {
      world.destroyBody(neighborEntityBodyMap.get(entity));
      neighborEntityBodyMap.remove(entity);
    }
  }

  synchronized void update() throws Exception {

    this.updateNeighbors();

    int tickTimeout = Integer.MAX_VALUE;

    for (Entity entity : this.chunkMap.values()) {
      if (entity.getEntityController() != null) entity.getEntityController().beforeWorldUpdate();
      try {
        this.gameStore.syncEntity(entity);
      } catch (EntityNotFound e) {
        LOGGER.error(e);
      }

      int entityTick = entity.getUpdateTimeout();
      if (entityTick < tickTimeout) {
        tickTimeout = entityTick;
      }
    }
    world.step(
        GameSettings.WORLD_TIME_STEP,
        GameSettings.WORLD_VELOCITY_ITERATIONS,
        GameSettings.WORLD_POSITION_ITERATIONS);

    for (Entity entity : this.chunkMap.values()) {
      if (entity.getEntityController() != null) entity.getEntityController().afterWorldUpdate();
    }
    this.nextTick(tickTimeout);
  }

  public List<Entity> getEntityInRange(
      Coordinates bottomLeftCoordinates, Coordinates topRightCoordinates) {

    List<Entity> entityList = new LinkedList<>();

    for (Entity entity : this.getEntityList()) {
      if (Coordinates.isInRange(bottomLeftCoordinates, topRightCoordinates, entity.coordinates)) {
        entityList.add(entity);
      }
    }

    return entityList;
  }

  public Block getBlock(Coordinates coordinates) throws EntityNotFound {
    List<Entity> entityList = this.getEntityInRange(coordinates, coordinates);
    for (Entity entity : entityList) {
      if (entity instanceof Block
          && Coordinates.isInRange(coordinates, coordinates, entity.coordinates)) {
        return (Block) entity;
      }
    }
    throw new EntityNotFound("could not find block at " + coordinates.toString());
  }

  public Ladder getLadder(Coordinates coordinates) {
    List<Entity> entityList = this.getEntityInRange(coordinates, coordinates);
    for (Entity entity : entityList) {
      if (entity instanceof Ladder
          && Coordinates.isInRange(coordinates, coordinates, entity.coordinates)) {

        return (Ladder) entity;
      }
    }
    return null;
  }

  public List<Entity> getEntityListBaseCoordinates(Coordinates coordinates) {
    coordinates = coordinates.getBase();
    return this.getEntityInRange(coordinates, coordinates);
  }

  @Override
  public NetworkObjects.NetworkData toNetworkData() {
    NetworkObjects.NetworkData.Builder networkDataBuilder = NetworkObjects.NetworkData.newBuilder();
    for (Entity entity : this.getEntityList()) {
      networkDataBuilder.addChildren(entity.toNetworkData());
    }
    networkDataBuilder.addChildren(this.chunkRange.toNetworkData());
    return networkDataBuilder.build();
  }

  public boolean equals(Object anObject) {
    if (this == anObject) {
      return true;
    }
    if (!(anObject instanceof Chunk)) {
      return false;
    }
    Chunk other = (Chunk) anObject;
    if (!this.chunkRange.equals(other.chunkRange)) return false;

    return this.getEntityUUIDSet().equals(other.getEntityUUIDSet());
  }
}

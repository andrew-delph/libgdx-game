package core.chunk;

import core.app.game.GameController;
import core.chunk.world.WorldWrapper;
import core.chunk.world.exceptions.BodyNotFound;
import core.chunk.world.exceptions.DestroyBodyException;
import com.badlogic.gdx.physics.box2d.World;
import core.common.Clock;
import core.common.GameStore;
import core.common.Tick;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.entity.collision.EntityContactListenerFactory;
import core.entity.misc.Ladder;
import core.entity.misc.Turret;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import networking.NetworkObjects;
import core.networking.events.interfaces.SerializeNetworkData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk implements Callable<Chunk>, SerializeNetworkData {

  final Logger LOGGER = LogManager.getLogger();
  final ConcurrentHashMap<UUID, Entity> chunkMap = new ConcurrentHashMap<>();
  private final WorldWrapper worldWrapper;
  private final Set<Entity> neighborEntitySet = new HashSet<>();
  public ChunkRange chunkRange;
  public Tick updateTick;
  GameStore gameStore;
  GameController gameController;
  Clock clock;

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
    this.nextTick(1);
    this.worldWrapper = new WorldWrapper(chunkRange);
    this.worldWrapper.applyWorld(
        (World world) ->
            world.setContactListener(entityContactListenerFactory.createEntityContactListener()));
  }

  public WorldWrapper getWorldWrapper() {
    return worldWrapper;
  }

  public void nextTick(int timeout) {
    this.updateTick = new Tick(clock.getCurrentTick().time + timeout);
  }

  @Override
  public Chunk call() throws Exception {
    try {
      this.update();
    } catch (Exception e) {
      LOGGER.error("CHUNK UPDATE FAILED: " + this, e);
    }
    return this;
  }

  public synchronized Entity removeEntity(UUID uuid) throws EntityNotFound, DestroyBodyException {
    Entity entity = this.getEntity(uuid);
    this.chunkMap.remove(uuid);
    synchronized (worldWrapper) {
      if (worldWrapper.hasBody(entity)) worldWrapper.destroyEntity(entity);
    }
    neighborEntitySet.add(entity);
    return entity;
  }

  public synchronized void addEntity(Entity entity) {
    this.chunkMap.put(entity.getUuid(), entity);
    this.addBody(entity);
    neighborEntitySet.remove(entity);
    this.nextTick(1);
  }

  public synchronized boolean addBody(Entity entity) {
    synchronized (worldWrapper) {
      if (worldWrapper.hasBody(entity)) return false;
      worldWrapper.addEntity(entity.addWorld(this));
    }
    return true;
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

  private void updateNeighbors() throws BodyNotFound, ChunkNotFound {
    Set<Entity> currentNeighborEntitySet = new HashSet<>();

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
      currentNeighborEntitySet.addAll(
          neighbor.getEntityInRange(neighborBottomLeft, neighborTopRight));
    }

    // check the difference
    Set<Entity> entityToAddSet = new HashSet<>(currentNeighborEntitySet);
    entityToAddSet.removeAll(neighborEntitySet);
    Set<Entity> entityToRemoveSet = new HashSet<>(neighborEntitySet);
    entityToRemoveSet.removeAll(currentNeighborEntitySet);

    // add temp entity to set
    for (Entity entity : entityToAddSet) {
      this.addBody(entity);
      neighborEntitySet.add(entity);
    }

    // remove temp entity from set
    for (Entity entity : entityToRemoveSet) {
      if (!worldWrapper.hasBody(entity)) continue;
      try {
        worldWrapper.destroyEntity(entity);
      } catch (DestroyBodyException e) {
        LOGGER.error(e);
      }
      neighborEntitySet.remove(entity);
    }

    for (Entity entity : currentNeighborEntitySet) {
      if (!worldWrapper.hasBody(entity)) {
        continue;
      }
      worldWrapper.setPosition(entity, entity.getBodyPosition());
    }
  }

  synchronized void update() throws Exception { // TODO this shouldnt throw all Exceptions
    this.updateNeighbors();

    int tickTimeout = Integer.MAX_VALUE;

    for (Entity entity : this.chunkMap.values()) {
      if (entity.getEntityController() != null) entity.getEntityController().beforeWorldUpdate();
      try {
        this.gameStore.syncEntity(entity);
      } catch (EntityNotFound e) {
        LOGGER.error(e);
      }
    }

    worldWrapper.tick();

    for (Entity entity : this.chunkMap.values()) {
      if (entity.getEntityController() != null) entity.getEntityController().afterWorldUpdate();

      int entityTick = entity.getUpdateTimeout();
      if (entityTick < tickTimeout) {
        tickTimeout = entityTick;
      }
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

  @Override
  public String toString() {
    return "Chunk{" + "chunkRange=" + chunkRange + '}';
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

  public Turret getTurret(Coordinates coordinates) {
    List<Entity> entityList = this.getEntityInRange(coordinates, coordinates);
    for (Entity entity : entityList) {
      if (entity instanceof Turret
          && Coordinates.isInRange(coordinates, coordinates, entity.coordinates)) {

        return (Turret) entity;
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

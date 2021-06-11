package infra.chunk;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.Clock;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.Tick;
import infra.entity.Entity;
import infra.entity.block.Block;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk implements Callable<Chunk> {

  public ChunkRange chunkRange;
  public Tick updateTick;
  GameStore gameStore;
  Clock clock;
  public World world;
  Map<UUID, Entity> chunkMap;
  Set<UUID> bodySet;

  @Inject
  public Chunk(Clock clock, GameStore gameStore, @Assisted ChunkRange chunkRange) {
    this.gameStore = gameStore;
    this.clock = clock;
    this.chunkRange = chunkRange;
    this.chunkMap = new ConcurrentHashMap();
    this.nextTick(1);
    this.bodySet = new HashSet<>();
    this.world = new World(new Vector2(0, -0.1f), false);
  }

  void nextTick(int timeout) {
    this.updateTick = new Tick(clock.currentTick.time + timeout);
  }

  @Override
  public Chunk call() throws Exception {
    try {
      this.update();
    } catch (Exception e) {
      System.out.println("chunk update");
      e.printStackTrace();
    }
    return this;
  }

  public synchronized void addEntity(Entity entity) {
    this.chunkMap.put(entity.uuid, entity);

    if (!bodySet.contains(entity.uuid)) {
      Body bodyToAdd = entity.addWorld(world);
      if(bodyToAdd != null){
        entity.setBody(bodyToAdd);
        bodySet.add(entity.uuid);
      }

    }
  }

  public Entity getEntity(UUID uuid) {
    return this.chunkMap.get(uuid);
  }

  public List<Entity> getEntityList() {
    return new LinkedList<>(this.chunkMap.values());
  }

  public Entity removeEntity(UUID uuid) {
    Entity entity = this.getEntity(uuid);
    this.chunkMap.remove(uuid);
    if (bodySet.contains(entity.uuid)) {
            System.out.println(
                "destroy body:"
                    + entity.uuid
                    + " ,"
                    + entity.coordinates
                    + " ,"
                    + entity.coordinates.getX()
                    + ","
                    + entity.coordinates.getY()
                    + ", "
                    + new ChunkRange(entity.coordinates)+", "+entity.getBody());
      this.world.destroyBody(entity.getBody());
      bodySet.remove(entity.uuid);
    }
    return entity;
  }

  Map<Entity, Body> neighborEntityBodyMap = new HashMap<>();

  synchronized void update() {
    Set<Entity> neighborEntitySet = new HashSet<>();

//    Boolean verbose = this.chunkRange.equals(new ChunkRange(new Coordinates(0,0)));

    Chunk neighborChunk = null;

    // up
    neighborChunk = this.gameStore.getChunk(this.chunkRange.getUp());
    if (!(neighborChunk == null)) {
      neighborEntitySet.addAll(
          neighborChunk.getEntityInRange(
              new Coordinates(neighborChunk.chunkRange.bottom_x, neighborChunk.chunkRange.bottom_y),
              new Coordinates(
                  neighborChunk.chunkRange.top_x, neighborChunk.chunkRange.bottom_y + 2)));
    }

    // down
    neighborChunk = this.gameStore.getChunk(this.chunkRange.getDown());
    if (!(neighborChunk == null)) {
      neighborEntitySet.addAll(
          neighborChunk.getEntityInRange(
              new Coordinates(neighborChunk.chunkRange.bottom_x, neighborChunk.chunkRange.top_y-2),
              new Coordinates(neighborChunk.chunkRange.top_x, neighborChunk.chunkRange.top_y)));
    }

    // left
    neighborChunk = this.gameStore.getChunk(this.chunkRange.getLeft());
    if (!(neighborChunk == null)) {
      neighborEntitySet.addAll(
          neighborChunk.getEntityInRange(
              new Coordinates(
                  neighborChunk.chunkRange.top_x - 2, neighborChunk.chunkRange.bottom_y),
              new Coordinates(neighborChunk.chunkRange.top_x, neighborChunk.chunkRange.top_y)));
    }

    // right
    neighborChunk = this.gameStore.getChunk(this.chunkRange.getRight());
    if (!(neighborChunk == null)) {
      neighborEntitySet.addAll(
          neighborChunk.getEntityInRange(
              new Coordinates(neighborChunk.chunkRange.bottom_x, neighborChunk.chunkRange.bottom_y),
              new Coordinates(
                  neighborChunk.chunkRange.bottom_x + 2, neighborChunk.chunkRange.top_y)));
    }

    // check the difference
    Set<Entity> entityToAddSet = new HashSet<>(neighborEntitySet);
    entityToAddSet.removeAll(neighborEntityBodyMap.keySet());
    Set<Entity> entityToRemoveSet = new HashSet<>(neighborEntityBodyMap.keySet());
    entityToRemoveSet.removeAll(neighborEntitySet);

    // add temp entity to set
    for (Entity entity: entityToAddSet){
      if(neighborEntityBodyMap.containsKey(entity)) continue;
      Body bodyToAdd = entity.addWorld(world);
      if (bodyToAdd == null) continue;
      neighborEntityBodyMap.put(entity,bodyToAdd);
    }

    // remove temp entity from set
    for(Entity entity: entityToRemoveSet){
      world.destroyBody(neighborEntityBodyMap.get(entity));
      neighborEntityBodyMap.remove(entity);
    }

    int tickTimeout = Integer.MAX_VALUE;
    for (Entity entity : this.chunkMap.values()) {

      entity.entityController.beforeWorldUpdate();
      this.gameStore.syncEntity(entity);

      int entityTick = entity.getUpdateTimeout();
      if (tickTimeout < entityTick) {
        tickTimeout = entityTick;
      }
    }
    world.step(1, 6, 2);

    for (Entity entity : this.chunkMap.values()) {
      entity.entityController.afterWorldUpdate();
    }
    this.nextTick(1);

  }

  public List<Entity> getEntityInRange(
      Coordinates bottomLeftCoordinates, Coordinates topRightCoordinates) {

    List<Entity> entityList = new LinkedList<>();

    for (Entity entity : this.getEntityList()) {
      if (Coordinates.inRange(bottomLeftCoordinates, topRightCoordinates, entity.coordinates)) {
        entityList.add(entity);
      }
    }

    return entityList;
  }

  public Block getBlock(Coordinates coordinates){
    List<Entity> entityList = this.getEntityInRange(coordinates,coordinates);
    for (Entity entity: entityList){
      if (entity instanceof Block && Coordinates.inRange(coordinates,coordinates,entity.coordinates)){
        return (Block) entity;
      }
    }
    return null;
  }
}

package infra.chunk;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.Tick;
import infra.entity.Entity;

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
    world = new World(new Vector2(0, -98f), true);
  }

  void nextTick(int timeout) {
    this.updateTick = new Tick(clock.currentTick.time + timeout);
  }

  @Override
  public Chunk call() throws Exception {
    try {
      this.update();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this;
  }

  public synchronized void addEntity(Entity entity) {
    this.chunkMap.put(entity.uuid, entity);
    if (!bodySet.contains(entity.uuid)) {
      entity.addWorld(world);
      bodySet.add(entity.uuid);
    }
  }

  public Entity getEntity(UUID uuid) {
    return this.chunkMap.get(uuid);
  }

  public List<Entity> getEntityList() {
    return new LinkedList<>(this.chunkMap.values());
  }

  public void removeEntity(UUID uuid) {
    this.chunkMap.remove(uuid);
  }

  synchronized void update() {
    int tickTimeout = Integer.MAX_VALUE;
    for (Entity entity : this.chunkMap.values()) {

      entity.entityController.beforeWorldUpdate();
      this.gameStore.syncEntity(entity);

      if (!(new ChunkRange(entity.coordinates).equals(this.chunkRange))) {
        this.removeEntity(entity.uuid);
        continue;
      }

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
}

package infra.chunk;

import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.Tick;
import infra.entity.Entity;
import infra.entity.EntityFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk implements Callable<Chunk> {

  public ChunkRange chunkRange;
  public Tick updateTick;
  GameStore gameStore;
  Clock clock;
  World world;
  Map<UUID, Entity> chunkMap;

  @Inject EntityFactory entityFactory;

  @Inject
  public Chunk(Clock clock, GameStore gameStore, @Assisted ChunkRange chunkRange) {
    this.gameStore = gameStore;
    this.clock = clock;
    this.chunkRange = chunkRange;
    this.chunkMap = new ConcurrentHashMap();
    this.nextTick(1);
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

  public void addEntity(Entity entity) {
    this.chunkMap.put(entity.uuid, entity);
  }

  public Entity getEntity(UUID uuid) {
    return this.chunkMap.get(uuid);
  }

  public void removeEntity(UUID uuid) {
    this.chunkMap.remove(uuid);
  }

  void update() {
    int tickTimeout = Integer.MAX_VALUE;
    for (Entity entity : this.chunkMap.values()) {

      entity.entityController.update();
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
    this.nextTick(1);
  }
}

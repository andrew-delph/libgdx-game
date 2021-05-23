package infra.chunk;

import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import infra.common.Clock;
import infra.common.GameStore;
import infra.common.Tick;
import infra.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class Chunk implements Callable<Chunk> {

  public ChunkRange chunkRange;
  public Tick updateTick;
  GameStore gameStore;
  Clock clock;
  World world;
  Map<UUID, Entity> chunkMap;

  @Inject
  public Chunk(Clock clock,GameStore gameStore, @Assisted  ChunkRange chunkRange) {
    this.gameStore = gameStore;
    this.clock = clock;
    this.chunkRange = chunkRange;
    this.chunkMap = new HashMap();
    this.nextTick(1);
  }

  void nextTick(int timeout) {
    this.updateTick = new Tick(clock.currentTick.time + timeout);
  }

  @Override
  public Chunk call() throws Exception {
    this.update();
    return this;
  }

  public void addEntity(Entity entity) {
    this.chunkMap.put(entity.uuid, entity);
  }

  public Entity getEntity(UUID uuid) {
    return this.chunkMap.get(uuid);
  }

  void update() {
    int tickTimeout = Integer.MAX_VALUE;
    for (Entity entity : this.chunkMap.values()) {
      entity.controller.update();
      this.gameStore.syncEntity(entity);

      int entityTick = entity.getUpdateTimeout();
      if (tickTimeout < entityTick) {
        tickTimeout = entityTick;
      }
    }
    this.nextTick(1);
  }
}

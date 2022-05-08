package entity.collision.projectile;

import app.GameController;
import com.google.inject.Inject;
import common.Clock;
import common.exceptions.ChunkNotFound;
import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProjectileContact implements ContactWrapper {

  @Inject GameController gameController;
  @Inject Clock clock;
  Set<UUID> removedAlready = new HashSet<>();

  public void init() {
    clock.addTaskOnTick(
        () -> {
          synchronized (ProjectileContact.class) {
            removedAlready = new HashSet<>();
          }
        });
  }

  @Override
  public void beginContact(CollisionPoint source, CollisionPoint target) {
    try {
      if (!target.getChunkRange().equals(target.getEntity().getChunk().chunkRange)) {
        return;
      }
    } catch (ChunkNotFound e) {
      e.printStackTrace();
    }
    synchronized (ProjectileContact.class) {
      if (removedAlready.contains(source.getEntity().getUuid())) {
        return;
      }
      removedAlready.add(source.getEntity().getUuid());
    }
    gameController.removeEntity(source.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

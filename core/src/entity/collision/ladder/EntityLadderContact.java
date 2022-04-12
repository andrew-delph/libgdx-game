package entity.collision.ladder;

import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.CollisionService;
import entity.collision.ContactWrapperCounter;
import java.util.HashMap;
import java.util.Map;

public class EntityLadderContact extends ContactWrapperCounter {

  @Inject CollisionService collisionService;

  Map<Body, Integer> ladderContactCounter = new HashMap<>();

  public boolean isOnLadder(Entity entity) throws ChunkNotFound {
    if (this.getContactCount(entity.uuid, entity.getChunk().chunkRange) == null
        || this.getContactCount(entity.uuid, entity.getChunk().chunkRange) > 0) {
      return true;
    } else return false;
  }
}

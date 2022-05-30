package entity.collision.ground;

import com.google.inject.Inject;
import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.collision.CollisionPoint;
import entity.collision.ContactWrapperCounter;

public class EntityGroundContact extends ContactWrapperCounter {

  @Inject
  public EntityGroundContact() {}

  @Override
  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    /*
     EntityFeetSensor.class, GroundSensor.class
     if the sensor is on its main. Update the last ground contact for that entity.
    */
    super.beginContact(source, target);
  }

  @Override
  public synchronized void endContact(CollisionPoint source, CollisionPoint target) {
    // EntityFeetSensor.class, GroundSensor.class
    super.endContact(source, target);
  }

  public Boolean isOnGround(Entity entity) throws ChunkNotFound {
    return this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) != null
        && this.getContactCount(entity.getUuid(), entity.getChunk().chunkRange) > 0;
  }
}

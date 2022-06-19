package entity.collision.ground;

import com.google.inject.Inject;
import common.events.EventService;
import common.exceptions.ChunkNotFound;
import entity.Entity;
import entity.attributes.Coordinates;
import entity.collision.CollisionPoint;
import entity.collision.ContactWrapperCounter;
import entity.controllers.events.types.EntityEventTypeFactory;
import entity.controllers.events.types.FallDamageEventType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityGroundContact extends ContactWrapperCounter {

  Map<UUID, Coordinates> uuidCoordinatesMap = new HashMap<>();

  @Inject EventService eventService;
  @Inject EntityEventTypeFactory entityEventTypeFactory;

  public EntityGroundContact() {}

  @Override
  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    /*
     EntityFeetSensor.class, GroundSensor.class
     if the sensor is on its main. Update the last ground contact for that entity.
    */
    try {
      if (source.getChunkRange().equals(source.getEntity().getChunk().chunkRange)) {
        if (uuidCoordinatesMap.get(source.getEntity().getUuid()) != null) {
          Coordinates oldPos = uuidCoordinatesMap.get(source.getEntity().getUuid());
          Coordinates newPos = source.getEntity().coordinates;
          FallDamageEventType fallEvent =
              entityEventTypeFactory.createFallDamageEventType(oldPos, newPos, source.getEntity());
          eventService.fireEvent(fallEvent);
        }
        uuidCoordinatesMap.put(source.getEntity().getUuid(), source.getEntity().coordinates);
      }
    } catch (ChunkNotFound e) {
      e.printStackTrace();
    }

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

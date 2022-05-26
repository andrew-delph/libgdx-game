package networking.events.consumer.server.incoming;

import app.GameController;
import app.user.User;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.CreateTurretEventType;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.ActiveEntityManager;
import entity.Entity;
import entity.misc.Turret;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateTurretIncomingConsumerServer implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();

  @Inject GameController gameController;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;
  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    CreateTurretEventType realEvent = (CreateTurretEventType) eventType;

    try {
      Entity entity = gameStore.getEntity(realEvent.getEntityUUID());
      Turret turret = gameController.createTurret(entity, realEvent.getCoordinates());
      if (turret != null)
        activeEntityManager.registerActiveEntity(user.getUserID(), turret.getUuid());
    } catch (ChunkNotFound | EntityNotFound e) {
      LOGGER.error(e, e);
    }
  }
}

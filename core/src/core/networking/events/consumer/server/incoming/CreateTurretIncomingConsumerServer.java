package core.networking.events.consumer.server.incoming;

import core.app.game.GameController;
import core.app.user.User;
import com.google.inject.Inject;
import core.common.GameStore;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.EventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.misc.Turret;
import core.entity.ActiveEntityManager;
import core.entity.Entity;
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

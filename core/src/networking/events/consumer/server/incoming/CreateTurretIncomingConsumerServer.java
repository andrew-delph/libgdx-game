package networking.events.consumer.server.incoming;

import app.GameController;
import app.user.User;
import com.google.inject.Inject;
import common.events.types.CreateTurretEventType;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import entity.ActiveEntityManager;
import entity.misc.Turret;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateTurretIncomingConsumerServer implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();

  @Inject GameController gameController;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;

  @Override
  public void accept(EventType eventType) {
    CreateTurretEventType realEvent = (CreateTurretEventType) eventType;
    try {
      Turret turret = gameController.createTurret(realEvent.getCoordinates());
      activeEntityManager.registerActiveEntity(user.getUserID(), turret.getUuid());
    } catch (ChunkNotFound e) {
      LOGGER.error(e, e);
    }
  }
}

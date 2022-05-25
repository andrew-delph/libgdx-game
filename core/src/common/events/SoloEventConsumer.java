package common.events;

import com.google.inject.Inject;
import common.events.types.CreateAIEntityEventType;
import common.events.types.CreateTurretEventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.controllers.EntityControllerFactory;
import entity.misc.Turret;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoloEventConsumer extends EventConsumer {

  final Logger LOGGER = LogManager.getLogger();
  @Inject EntityControllerFactory entityControllerFactory;

  @Override
  public void init() {
    super.init();

    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type,
        eventType -> {
          try {
            CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;
            LOGGER.info("CREATE AI " + realEvent.getCoordinates());

            Entity aiEntity = gameController.createEntity(realEvent.getCoordinates());

            Entity aiTarget = gameStore.getEntity(realEvent.getTarget());

            aiEntity.setEntityController(
                entityControllerFactory.createEntityPathController(aiEntity, aiTarget));

          } catch (EntityNotFound e) {
            e.printStackTrace();
          } catch (ChunkNotFound e) {
            e.printStackTrace();
          }
        });
    this.eventService.addPostUpdateListener(
        CreateTurretEventType.type,
        event -> {
          CreateTurretEventType realEvent = (CreateTurretEventType) event;
          try {
            Entity entity = gameStore.getEntity(realEvent.getEntityUUID());
            Turret turret = gameController.createTurret(entity, realEvent.getCoordinates());
            activeEntityManager.registerActiveEntity(user.getUserID(), turret.getUuid());
          } catch (ChunkNotFound | EntityNotFound e) {
            LOGGER.error(e, e);
          }
        });
  }
}

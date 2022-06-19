package common.events;

import app.GameController;
import app.user.User;
import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.RemoveEntityEventType;
import common.events.types.ReplaceEntityEventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.ActiveEntityManager;
import entity.controllers.events.types.AbstractEntityEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventConsumer {

  final Logger LOGGER = LogManager.getLogger();

  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject GameStore gameStore;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;

  @Inject
  public EventConsumer() {}

  public void init() {
    this.eventService.addPostUpdateListener(
        ReplaceEntityEventType.type,
        event -> {
          ReplaceEntityEventType realEvent = (ReplaceEntityEventType) event;
          try {
            this.gameController.triggerReplaceEntity(
                realEvent.getTargetUUID(),
                realEvent.getReplacementEntity(),
                realEvent.getSwapVelocity());
          } catch (EntityNotFound | BodyNotFound | DestroyBodyException | ChunkNotFound e) {
            LOGGER.error(e, e);
          }
        });
    this.eventService.addPostUpdateListener(
        RemoveEntityEventType.type,
        event -> {
          RemoveEntityEventType realEvent = (RemoveEntityEventType) event;
          try {
            this.gameStore.removeEntity(realEvent.getEntityUUID());
            activeEntityManager.removeActiveEntity(user.getUserID(), realEvent.getEntityUUID());
          } catch (EntityNotFound | DestroyBodyException e) {
            LOGGER.error(e, e);
          }
        });
    this.eventService.addListener(
        AbstractEntityEventType.type,
        event -> {
          AbstractEntityEventType entityEvent = (AbstractEntityEventType) event;
          if (entityEvent.getEntity().getEntityController() != null)
            entityEvent.getEntity().getEntityController().fireEvent(entityEvent);
        });
  }
}

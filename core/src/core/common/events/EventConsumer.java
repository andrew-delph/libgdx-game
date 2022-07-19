package core.common.events;

import core.app.game.GameController;
import core.app.user.User;
import core.chunk.world.exceptions.BodyNotFound;
import core.chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import core.common.GameStore;
import core.common.events.types.RemoveEntityEventType;
import core.common.events.types.ReplaceEntityEventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.ActiveEntityManager;
import core.entity.controllers.events.types.AbstractEntityEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventConsumer {

  final Logger LOGGER = LogManager.getLogger();

  @Inject EventService eventService;
  @Inject
  GameController gameController;
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

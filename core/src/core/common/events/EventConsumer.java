package core.common.events;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.game.GameController;
import core.app.user.User;
import core.chunk.world.exceptions.BodyNotFound;
import core.chunk.world.exceptions.DestroyBodyException;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.types.CreateEntityEventType;
import core.common.events.types.RemoveEntityEventType;
import core.common.events.types.ReplaceEntityEventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.ActiveEntityManager;
import core.entity.controllers.events.types.AbstractEntityEventType;

public class EventConsumer {

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
            Gdx.app.error(GameSettings.LOG_TAG, e.getMessage(), e);
          }
        });
    this.eventService.addPostUpdateListener(
        RemoveEntityEventType.type,
        event -> {
          RemoveEntityEventType realEvent = (RemoveEntityEventType) event;
          try {
            gameController.triggerRemoveEntity(realEvent.getEntityUUID());
          } catch (EntityNotFound | DestroyBodyException e) {
            Gdx.app.error(GameSettings.LOG_TAG, e.getMessage(), e);
          }
        });
    this.eventService.addListener(
        AbstractEntityEventType.type,
        event -> {
          AbstractEntityEventType entityEvent = (AbstractEntityEventType) event;
          if (entityEvent.getEntity().getEntityController() != null)
            entityEvent.getEntity().getEntityController().fireEvent(entityEvent);
        });

    this.eventService.addPostUpdateListener(
        CreateEntityEventType.type,
        event -> {
          CreateEntityEventType entityEvent = (CreateEntityEventType) event;
          try {
            gameController.addEntity(entityEvent.getEntity());
          } catch (ChunkNotFound e) {
            e.printStackTrace();
          }
        });
  }
}

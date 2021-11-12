package common.events;

import com.google.inject.Inject;
import app.GameController;
import common.GameStore;

public class EventConsumer {

  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject GameStore gameStore;

  @Inject
  public EventConsumer() {}

  public void init() {
    this.eventService.addPostUpdateListener(
        ReplaceBlockEventType.type,
        event -> {
          ReplaceBlockEventType realEvent = (ReplaceBlockEventType) event;
          this.gameController.replaceBlock(realEvent.getTarget(), realEvent.getReplacementBlock());
        });

    this.eventService.addPostUpdateListener(
        RemoveEntityEventType.type,
        event -> {
          RemoveEntityEventType realEvent = (RemoveEntityEventType) event;
          this.gameStore.removeEntity(realEvent.getEntityUUID());
        });
  }
}

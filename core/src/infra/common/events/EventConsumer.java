package infra.common.events;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.common.GameStore;

public class EventConsumer {

  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject GameStore gameStore;

  @Inject
  public EventConsumer() {}

  public void init() {
    this.eventService.addPostUpdateListener(
        ReplaceBlockEvent.type,
        event -> {
          ReplaceBlockEvent realEvent = (ReplaceBlockEvent) event;
          this.gameController.replaceBlock(
              realEvent.getTarget(), realEvent.getReplacementBlockType());
        });

    this.eventService.addPostUpdateListener(
        RemoveEntityEvent.type,
        event -> {
          RemoveEntityEvent realEvent = (RemoveEntityEvent) event;
          this.gameStore.removeEntity(realEvent.getEntityUUID());
        });
  }
}

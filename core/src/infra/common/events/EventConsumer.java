package infra.common.events;

import com.google.inject.Inject;
import infra.common.GameStore;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;
import infra.networking.events.ReplaceBlockOutgoingEvent;

public class EventConsumer {

  @Inject EventService eventService;
  @Inject GameStore gameStore;
  @Inject BlockFactory blockFactory;

  @Inject
  public EventConsumer() {}

  public void init() {
    this.eventService.addListener(
        ReplaceBlockOutgoingEvent.type,
        event -> {
          ReplaceBlockOutgoingEvent realEvent = (ReplaceBlockOutgoingEvent) event;
          Block removeBlock = (Block) this.gameStore.removeEntity(realEvent.getTarget());
          if (removeBlock == null) return;
          Block newBlock = blockFactory.createSky();
          newBlock.coordinates = removeBlock.coordinates;
          this.gameStore.addEntity(newBlock);
        });
  }
}

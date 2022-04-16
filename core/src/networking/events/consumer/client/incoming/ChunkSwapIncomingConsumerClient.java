package networking.events.consumer.client.incoming;

import chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import java.util.function.Consumer;
import networking.events.types.incoming.ChunkSwapIncomingEventType;

public class ChunkSwapIncomingConsumerClient implements Consumer<EventType> {

  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    ChunkSwapIncomingEventType incoming = (ChunkSwapIncomingEventType) eventType;
    if (!gameStore.doesEntityExist(incoming.getTarget())) return;
    if (!gameStore.doesChunkExist(incoming.getTo())) {
      try {
        gameStore.removeEntity(incoming.getTarget());
      } catch (EntityNotFound | DestroyBodyException e) {
        e.printStackTrace();
      }
    }
  }
}

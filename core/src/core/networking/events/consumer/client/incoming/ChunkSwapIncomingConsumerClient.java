package core.networking.events.consumer.client.incoming;

import core.chunk.world.exceptions.DestroyBodyException;
import com.google.inject.Inject;
import core.common.GameStore;
import core.common.events.types.EventType;
import core.common.exceptions.EntityNotFound;
import core.networking.events.types.incoming.ChunkSwapIncomingEventType;
import java.util.function.Consumer;

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

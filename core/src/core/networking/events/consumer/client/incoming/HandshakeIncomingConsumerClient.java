package core.networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import core.chunk.Chunk;
import core.chunk.world.exceptions.DestroyBodyException;
import core.common.ChunkRange;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.events.types.EventType;
import core.common.exceptions.EntityNotFound;
import core.entity.ActiveEntityManager;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.HandshakeIncomingEventType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandshakeIncomingConsumerClient implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject GameStore gameStore;
  @Inject EventService eventService;
  @Inject ActiveEntityManager activeEntityManager;

  @Override
  public void accept(EventType eventType) {
    HandshakeIncomingEventType handshakeIncoming = (HandshakeIncomingEventType) eventType;

    ChunkRange chunkRange = handshakeIncoming.getChunkRange();
    Chunk chunk = gameStore.getChunk(chunkRange);

    // get the missing uuid on the client
    List<UUID> missing = new LinkedList<>(handshakeIncoming.getListUUID());
    missing.removeAll(chunk.getEntityUUIDSet());

    // get the extra uuid on the client
    List<UUID> extra = new ArrayList<>(chunk.getEntityUUIDSet());
    extra.removeAll(handshakeIncoming.getListUUID());
    extra.removeAll(activeEntityManager.getActiveEntities());

    // remove the extra
    for (UUID toRemove : extra) {
      try {
        chunk.removeEntity(toRemove);
      } catch (EntityNotFound | DestroyBodyException e) {
        LOGGER.error(e);
      }
    }

    // request the missing
    if (missing.size() > 0)
      eventService.fireEvent(
          EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, missing));
  }
}

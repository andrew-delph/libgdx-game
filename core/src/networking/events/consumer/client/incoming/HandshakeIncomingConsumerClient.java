package networking.events.consumer.client.incoming;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.HandshakeIncomingEventType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class HandshakeIncomingConsumerClient implements Consumer<EventType> {

  private static final Logger LOGGER = Logger.getLogger(GameStore.class.getName());
  @Inject GameStore gameStore;
  @Inject EventService eventService;

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

    // remove the extra
    for (UUID toRemove : extra) {
      try {
        chunk.removeEntity(toRemove);
      } catch (EntityNotFound e) {
        e.printStackTrace();
        LOGGER.severe("Entity already removed in handshake.");
      }
    }

    // request the missing
    if (missing.size() > 0)
      eventService.fireEvent(
          EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, missing));
  }
}

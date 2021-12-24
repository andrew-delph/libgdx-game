package networking.events.consumer.client.incoming;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.types.EventType;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.HandshakeIncomingEventType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class HandshakeIncomingConsumerClient implements Consumer<EventType> {

    @Inject
    GameStore gameStore;
    @Inject
    EventService eventService;

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
            chunk.removeEntity(toRemove);
        }

        // request the missing
        if (missing.size() > 0)
            eventService.fireEvent(EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, missing));
    }
}

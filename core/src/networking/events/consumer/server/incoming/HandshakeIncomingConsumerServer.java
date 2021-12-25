package networking.events.consumer.server.incoming;

import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import entity.Entity;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.HandshakeIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class HandshakeIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    GameStore gameStore;
    @Inject
    ServerNetworkHandle serverNetworkHandle;
    @Inject
    EventTypeFactory eventTypeFactory;

    @Override
    public void accept(EventType eventType) {
        HandshakeIncomingEventType handshakeIncoming = (HandshakeIncomingEventType) eventType;

        ChunkRange chunkRange = handshakeIncoming.getChunkRange();
        UUID clientUUID = handshakeIncoming.getRequestUUID();

        List<UUID> missingUUIDList = new LinkedList<>(handshakeIncoming.getListUUID());

        if (missingUUIDList.size() > 0) {
            // need to get the uuid of the client
            // send each entity to the client
            List<Entity> missingEntityList = this.gameStore.getEntityListFromList(missingUUIDList);

            for (Entity missingEntity : missingEntityList) {
                CreateEntityOutgoingEventType createEntityOutgoing = eventTypeFactory.createCreateEntityOutgoingEvent(missingEntity.toNetworkData());
                this.serverNetworkHandle.send(clientUUID, createEntityOutgoing.toNetworkEvent());
            }
        } else {
            this.serverNetworkHandle.initHandshake(clientUUID, chunkRange);
        }
    }
}

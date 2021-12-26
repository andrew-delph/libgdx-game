package networking.events.consumer.server.incoming;

import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import entity.block.Block;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataDeserializer;

import java.util.UUID;
import java.util.function.Consumer;

public class ReplaceBlockIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    EventService eventService;
    @Inject
    NetworkDataDeserializer entitySerializationConverter;
    @Inject
    ChunkSubscriptionService chunkSubscriptionService;
    @Inject
    ServerNetworkHandle serverNetworkHandle;
    @Inject
    GameStore gameStore;
    @Inject
    EventTypeFactory eventTypeFactory;

    @Override
    public void accept(EventType eventType) {
        ReplaceBlockIncomingEventType realEvent = (ReplaceBlockIncomingEventType) eventType;
        Entity placedEntity = null;
        try {
            placedEntity = this.gameStore.getEntity(realEvent.getTarget());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            // TODO init handshake with the client
            return;
        }
        ChunkRange chunkRange = new ChunkRange(placedEntity.coordinates);
        try {
            this.eventService.queuePostUpdateEvent(
                    this.eventTypeFactory.createReplaceBlockEvent(
                            realEvent.getTarget(),
                            (Block) entitySerializationConverter.createEntity(realEvent.getReplacementBlockData())));
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO disconnect the client
            return;
        }
        for (UUID uuid : chunkSubscriptionService.getSubscriptions(chunkRange)) {
            serverNetworkHandle.send(uuid, realEvent.networkEvent);
        }
    }
}

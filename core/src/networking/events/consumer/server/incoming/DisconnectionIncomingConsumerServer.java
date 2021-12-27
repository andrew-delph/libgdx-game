package networking.events.consumer.server.incoming;

import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import generation.ChunkGenerationManager;
import networking.ConnectionStore;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.DisconnectionIncomingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DisconnectionIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    EventService eventService;
    @Inject
    ChunkSubscriptionService chunkSubscriptionService;
    @Inject
    ServerNetworkHandle serverNetworkHandle;
    @Inject
    GameStore gameStore;
    @Inject
    EventTypeFactory eventTypeFactory;
    @Inject
    ChunkGenerationManager chunkGenerationManager;
    @Inject
    ConnectionStore connectionStore;

    private final static Logger LOGGER = Logger.getLogger(GameStore.class.getName());

    @Override
    public void accept(EventType eventType) {
        DisconnectionIncomingEventType realEvent = (DisconnectionIncomingEventType) eventType;
        connectionStore.removeConnection(realEvent.getUuid());
        for (UUID ownersEntityUuid : chunkGenerationManager.getOwnerUuidList(realEvent.getUuid())) {
            Entity entity = null;
            try {
                entity = this.gameStore.getEntity(ownersEntityUuid);
            } catch (EntityNotFound e) {
                LOGGER.severe("DISCONNECT COULD NOT FIND AN ENTITY IT OWNS");
                e.printStackTrace();
                continue;
            }
            this.eventService.queuePostUpdateEvent(
                    eventTypeFactory.createRemoveEntityEvent(ownersEntityUuid));

            RemoveEntityOutgoingEventType removeEntityOutgoingEvent = EventTypeFactory.createRemoveEntityOutgoingEvent(
                    entity.uuid, new ChunkRange(entity.coordinates));
            
            for (UUID subscriptionUuid :
                    chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
                serverNetworkHandle.send(subscriptionUuid, removeEntityOutgoingEvent.toNetworkEvent());
            }
        }
    }
}

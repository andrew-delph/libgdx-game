package networking.events.consumer.server.incoming;

import app.GameController;
import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import generation.ChunkGenerationManager;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataDeserializer;

import java.util.UUID;
import java.util.function.Consumer;

public class CreateEntityIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    GameController gameController;
    @Inject
    NetworkDataDeserializer entitySerializationConverter;
    @Inject
    ChunkSubscriptionService chunkSubscriptionService;
    @Inject
    ServerNetworkHandle serverNetworkHandle;
    @Inject
    ChunkGenerationManager chunkGenerationManager;

    @Override
    public void accept(EventType eventType) {
        CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) eventType;
        Entity entity =
                null;
        try {
            entity = gameController.triggerCreateEntity(
                    entitySerializationConverter.createEntity(realEvent.getData()));
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO init a handshake with the client. to remove the entity
            return;
        }
        chunkGenerationManager.registerActiveEntity(
                entity, UUID.fromString(realEvent.networkEvent.getUser()));

        for (UUID uuid : chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
            if (uuid.equals(realEvent.getUser())) continue;
            serverNetworkHandle.send(uuid, realEvent.networkEvent);
        }
    }
}

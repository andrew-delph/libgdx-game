package networking.events.consumer.server.incoming;

import app.GameController;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import generation.ChunkGenerationManager;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
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
        CreateEntityIncomingEventType incoming = (CreateEntityIncomingEventType) eventType;
        Entity entity = null;
        try {
            entity = gameController.triggerCreateEntity(
                    entitySerializationConverter.createEntity(incoming.getData()));
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO test this
            serverNetworkHandle.initHandshake(incoming.getUser(), incoming.getChunkRange());
            return;
        }

        chunkGenerationManager.registerActiveEntity(entity, incoming.getUser());

        CreateEntityOutgoingEventType outgoing = EventTypeFactory.createCreateEntityOutgoingEvent(incoming.getData(), incoming.getChunkRange());

        for (UUID uuid : chunkSubscriptionService.getSubscriptions(incoming.getChunkRange())) {
            if (uuid.equals(incoming.getUser())) continue;
            serverNetworkHandle.send(uuid, outgoing.toNetworkEvent());
        }
    }
}

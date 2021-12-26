package networking.events.consumer.server.incoming;

import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataDeserializer;

import java.util.UUID;
import java.util.function.Consumer;

public class UpdateEntityIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    NetworkDataDeserializer entitySerializationConverter;
    @Inject
    ChunkSubscriptionService chunkSubscriptionService;
    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Override
    public void accept(EventType eventType) {
        UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
        Entity entity = null;
        try {
            entity = entitySerializationConverter.updateEntity(realEvent.getData());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            // TODO handshake with the client
            return;
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO disconnect client for now.
        }
        for (UUID uuid : chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
            if (uuid.equals(realEvent.getUser())) continue;
            serverNetworkHandle.send(uuid, realEvent.networkEvent);
        }
    }
}

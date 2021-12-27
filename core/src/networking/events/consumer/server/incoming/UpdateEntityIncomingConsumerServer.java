package networking.events.consumer.server.incoming;

import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
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
        UpdateEntityIncomingEventType incoming = (UpdateEntityIncomingEventType) eventType;
        try {
            entitySerializationConverter.updateEntity(incoming.getData());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            // TODO test this
            serverNetworkHandle.initHandshake(incoming.getUser(), incoming.getChunkRange());
            return;
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO disconnect client for now.
        }
        UpdateEntityOutgoingEventType outgoing = EventTypeFactory.createUpdateEntityOutgoingEvent(incoming.getData(), incoming.getChunkRange());
        for (UUID uuid : chunkSubscriptionService.getSubscriptions(outgoing.getChunkRange())) {
            if (uuid.equals(incoming.getUser())) continue;
            serverNetworkHandle.send(uuid, outgoing.toNetworkEvent());
        }
    }
}

package networking.events.consumer.server.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CreateEntityOutgoingConsumerServer implements Consumer<EventType> {

    @Inject
    ChunkSubscriptionManager chunkSubscriptionManager;
    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Override
    public void accept(EventType eventType) {
        CreateEntityOutgoingEventType realEvent = (CreateEntityOutgoingEventType) eventType;
        NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
        List<UUID> uuidList = chunkSubscriptionManager.getSubscriptions(realEvent.getChunkRange());
        for (UUID uuid : uuidList) {
            serverNetworkHandle.send(uuid, networkEvent);
        }
    }
}

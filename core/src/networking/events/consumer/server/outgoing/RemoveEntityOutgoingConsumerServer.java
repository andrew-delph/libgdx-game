package networking.events.consumer.server.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class RemoveEntityOutgoingConsumerServer implements Consumer<EventType> {
    @Inject
    ChunkSubscriptionManager chunkSubscriptionManager;
    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Override
    public void accept(EventType eventType) {
        RemoveEntityOutgoingEventType outgoing = (RemoveEntityOutgoingEventType) eventType;
        for (UUID uuid : chunkSubscriptionManager.getSubscriptions(outgoing.getChunkRange())) {
            serverNetworkHandle.send(uuid, outgoing.toNetworkEvent());
        }
    }
}

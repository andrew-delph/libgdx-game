package networking.events.consumer.server.outgoing;

import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.types.EventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class RemoveEntityOutgoingConsumerServer implements Consumer<EventType> {
    @Inject
    ChunkSubscriptionService chunkSubscriptionService;
    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Override
    public void accept(EventType eventType) {
        RemoveEntityOutgoingEventType outgoing = (RemoveEntityOutgoingEventType) eventType;
        for (UUID uuid : chunkSubscriptionService.getSubscriptions(outgoing.getChunkRange())) {
            serverNetworkHandle.send(uuid, outgoing.toNetworkEvent());
        }
    }
}

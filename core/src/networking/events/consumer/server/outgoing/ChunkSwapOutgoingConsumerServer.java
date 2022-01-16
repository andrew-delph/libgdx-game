package networking.events.consumer.server.outgoing;

import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ChunkSwapOutgoingConsumerServer implements Consumer<EventType> {

    @Inject
    ChunkSubscriptionService chunkSubscriptionService;
    @Inject
    ServerNetworkHandle serverNetworkHandle;


    @Override
    public void accept(EventType eventType) {
        ChunkSwapOutgoingEventType outgoing = (ChunkSwapOutgoingEventType) eventType;
        NetworkObjects.NetworkEvent networkEvent = outgoing.toNetworkEvent();
        List<UUID> uuidList = chunkSubscriptionService.getSubscriptions(outgoing.getFrom());
        for (UUID uuid : uuidList) {
            serverNetworkHandle.send(uuid, networkEvent);
        }
    }
}

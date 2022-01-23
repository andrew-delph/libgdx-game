package networking.events.consumer.server.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class ReplaceBlockIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    ChunkSubscriptionManager chunkSubscriptionManager;
    @Inject
    ServerNetworkHandle serverNetworkHandle;
    @Inject
    GameController gameController;

    @Override
    public void accept(EventType eventType) {
        ReplaceBlockIncomingEventType incoming = (ReplaceBlockIncomingEventType) eventType;
        try {
            gameController.triggerReplaceEntity(incoming.getTarget(), incoming.getReplacementBlock());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            serverNetworkHandle.initHandshake(incoming.getUser(), incoming.getChunkRange());
        }
        ReplaceBlockOutgoingEventType outgoing = EventTypeFactory.createReplaceBlockOutgoingEvent(
                incoming.getTarget(),
                incoming.getReplacementBlock(),
                incoming.getChunkRange());
        for (UUID uuid : chunkSubscriptionManager.getSubscriptions(incoming.getChunkRange())) {
            if (incoming.getUser().equals(uuid)) continue;
            serverNetworkHandle.send(uuid, outgoing.toNetworkEvent());
        }
    }
}

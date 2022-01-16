package networking.events.consumer.server.incoming;

import app.GameController;
import chunk.ChunkSubscriptionManager;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class RemoveEntityIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    GameController gameController;
    @Inject
    ChunkSubscriptionManager chunkSubscriptionManager;
    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Override
    public void accept(EventType eventType) {
        RemoveEntityIncomingEventType incoming = (RemoveEntityIncomingEventType) eventType;

        try {
            gameController.triggerRemoveEntity(incoming.getTarget());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            this.serverNetworkHandle.initHandshake(incoming.getUser(), incoming.getChunkRange());
        }

        RemoveEntityOutgoingEventType outgoing = EventTypeFactory.createRemoveEntityOutgoingEvent(incoming.getTarget(), incoming.getChunkRange());

        for (UUID uuid : chunkSubscriptionManager.getSubscriptions(incoming.getChunkRange())) {
            if (uuid.equals(incoming.getUser())) continue;
            serverNetworkHandle.send(uuid, outgoing.toNetworkEvent());
        }
    }
}

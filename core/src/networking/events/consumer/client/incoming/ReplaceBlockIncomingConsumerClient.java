package networking.events.consumer.client.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;

import java.util.function.Consumer;

public class ReplaceBlockIncomingConsumerClient implements Consumer<EventType> {
    @Inject
    GameController gameController;
    @Inject
    ClientNetworkHandle clientNetworkHandle;

    @Override
    public void accept(EventType eventType) {
        ReplaceBlockIncomingEventType incoming = (ReplaceBlockIncomingEventType) eventType;
        try {
            gameController.triggerReplaceBlock(incoming.getTarget(), incoming.getReplacementBlock());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            clientNetworkHandle.initHandshake(incoming.getChunkRange());
        }
    }
}

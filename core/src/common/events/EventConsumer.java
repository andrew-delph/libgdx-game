package common.events;

import app.GameController;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.RemoveEntityEventType;
import common.events.types.ReplaceBlockEventType;
import common.exceptions.EntityNotFound;

public class EventConsumer {

    @Inject
    EventService eventService;
    @Inject
    GameController gameController;
    @Inject
    GameStore gameStore;

    @Inject
    public EventConsumer() {
    }

    public void init() {
        this.eventService.addPostUpdateListener(
                common.events.types.ReplaceBlockEventType.type,
                event -> {
                    common.events.types.ReplaceBlockEventType realEvent = (ReplaceBlockEventType) event;
                    try {
                        this.gameController.replaceBlock(realEvent.getTarget(), realEvent.getReplacementBlock());
                    } catch (EntityNotFound e) {
                        e.printStackTrace();
                        // TODO test this. maybe we don't need chunk range here..
                        gameController.initHandshake(realEvent.getChunkRange());
                    }
                });
        this.eventService.addPostUpdateListener(
                common.events.types.RemoveEntityEventType.type,
                event -> {
                    common.events.types.RemoveEntityEventType realEvent = (RemoveEntityEventType) event;
                    try {
                        this.gameStore.removeEntity(realEvent.getEntityUUID());
                    } catch (EntityNotFound e) {
                        e.printStackTrace();
                        // removing an entity that doesn't exist doesn't need a handshake
                    }
                });
    }
}

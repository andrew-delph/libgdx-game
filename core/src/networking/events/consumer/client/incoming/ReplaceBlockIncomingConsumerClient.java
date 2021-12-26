package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;

import java.util.function.Consumer;

public class ReplaceBlockIncomingConsumerClient implements Consumer<EventType> {

    @Inject
    EventService eventService;

    @Override
    public void accept(EventType eventType) {
        ReplaceBlockIncomingEventType realEvent = (ReplaceBlockIncomingEventType) eventType;
        this.eventService.queuePostUpdateEvent(
                EventTypeFactory.createReplaceBlockEvent(
                        realEvent.getTarget(),
                        realEvent.getReplacementBlock(), realEvent.getChunkRange()));
    }
}

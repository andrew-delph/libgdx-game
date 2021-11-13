package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.EventType;
import entity.Entity;
import entity.EntitySerializationConverter;
import networking.events.EventFactory;
import networking.events.types.incoming.RemoveEntityIncomingEventType;

import java.util.function.Consumer;

public class RemoveEntityIncomingConsumerClient implements Consumer<EventType> {

    @Inject
    EventService eventService;
    @Inject
    EntitySerializationConverter entitySerializationConverter;
    @Inject
    EventFactory eventFactory;


    @Override
    public void accept(EventType eventType) {
        RemoveEntityIncomingEventType realEvent = (RemoveEntityIncomingEventType) eventType;
        Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
        eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
    }
}

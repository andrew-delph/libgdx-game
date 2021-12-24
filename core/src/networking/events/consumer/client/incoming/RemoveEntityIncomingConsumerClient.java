package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import entity.Entity;
import networking.translation.NetworkDataDeserializer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.RemoveEntityIncomingEventType;

import java.util.function.Consumer;

public class RemoveEntityIncomingConsumerClient implements Consumer<EventType> {

    @Inject
    EventService eventService;
    @Inject
    NetworkDataDeserializer entitySerializationConverter;
    @Inject
    EventTypeFactory eventTypeFactory;

    @Override
    public void accept(EventType eventType) {
        RemoveEntityIncomingEventType realEvent = (RemoveEntityIncomingEventType) eventType;
        Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
        eventService.queuePostUpdateEvent(eventTypeFactory.createRemoveEntityEvent(entity.uuid));
    }
}

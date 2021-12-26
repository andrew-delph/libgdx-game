package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.EventService;
import common.events.types.EventType;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;

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
        Entity entity = null;
        try {
            entity = entitySerializationConverter.createEntity(realEvent.getData());
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // disconnect the client
            return;
        }
        eventService.queuePostUpdateEvent(eventTypeFactory.createRemoveEntityEvent(entity.uuid));
    }
}

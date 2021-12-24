package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;

import java.util.function.Consumer;

public class UpdateEntityIncomingConsumerClient implements Consumer<EventType> {

    @Inject
    NetworkDataDeserializer entitySerializationConverter;

    @Override
    public void accept(EventType eventType) {
        UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
        entitySerializationConverter.updateEntity(realEvent.getData());
    }
}

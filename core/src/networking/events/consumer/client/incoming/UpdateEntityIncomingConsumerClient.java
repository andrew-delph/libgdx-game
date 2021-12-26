package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;

import java.util.function.Consumer;

public class UpdateEntityIncomingConsumerClient implements Consumer<EventType> {

    @Inject
    NetworkDataDeserializer entitySerializationConverter;

    @Override
    public void accept(EventType eventType) {
        UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
        try {
            entitySerializationConverter.updateEntity(realEvent.getData());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            // TODO init handshake
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO disconnect client
        }
    }
}

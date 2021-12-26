package networking.events.consumer.client.incoming;

import app.GameController;
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
    @Inject
    GameController gameController;

    @Override
    public void accept(EventType eventType) {
        UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
        try {
            entitySerializationConverter.updateEntity(realEvent.getData());
        } catch (EntityNotFound e) {
            e.printStackTrace();
            // TODO test this
            gameController.initHandshake(null);
        } catch (SerializationDataMissing e) {
            e.printStackTrace();
            // TODO disconnect client
        }
    }
}

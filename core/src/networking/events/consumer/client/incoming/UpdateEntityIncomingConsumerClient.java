package networking.events.consumer.client.incoming;

import app.GameController;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;

public class UpdateEntityIncomingConsumerClient implements Consumer<EventType> {

  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject GameController gameController;
  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
    try {
      entitySerializationConverter.updateEntity(realEvent.getData());
    } catch (EntityNotFound e) {
      e.printStackTrace();
      // TODO test this
      clientNetworkHandle.initHandshake(realEvent.getChunkRange());
    } catch (SerializationDataMissing e) {
      e.printStackTrace();
      // TODO disconnect client
    }
  }
}

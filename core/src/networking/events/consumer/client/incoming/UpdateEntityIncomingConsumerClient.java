package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.translation.NetworkDataDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateEntityIncomingConsumerClient implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;
    try {
      entitySerializationConverter.updateEntity(realEvent.getData());
    } catch (EntityNotFound e) {
      LOGGER.error(e);
      clientNetworkHandle.initHandshake(realEvent.getChunkRange());
    } catch (SerializationDataMissing e) {
      LOGGER.error(e);
      // TODO disconnect client
    }
  }
}

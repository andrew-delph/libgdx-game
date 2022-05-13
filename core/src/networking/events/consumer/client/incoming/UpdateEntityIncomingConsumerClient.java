package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.attributes.Attribute;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateEntityIncomingConsumerClient implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject ClientNetworkHandle clientNetworkHandle;
  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) eventType;

    Entity entity;
    try {
      entity = gameStore.getEntity(realEvent.getUuid());
    } catch (EntityNotFound e) {
      LOGGER.error(e);
      clientNetworkHandle.initHandshake(realEvent.getChunkRange());
      return;
    }

    for (Attribute attr : realEvent.getAttributeList()) {
      entity.updateAttribute(attr);
    }
  }
}

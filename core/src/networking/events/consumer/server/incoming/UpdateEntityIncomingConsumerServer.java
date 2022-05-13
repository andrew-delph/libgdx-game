package networking.events.consumer.server.incoming;

import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.attributes.Attribute;
import java.util.function.Consumer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataDeserializer;

public class UpdateEntityIncomingConsumerServer implements Consumer<EventType> {

  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject ActiveChunkManager activeChunkManager;
  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    UpdateEntityIncomingEventType incoming = (UpdateEntityIncomingEventType) eventType;
    Entity entity;
    try {
      entity = gameStore.getEntity(incoming.getUuid());
    } catch (EntityNotFound e) {
      e.printStackTrace();
      // TODO test this
      serverNetworkHandle.initHandshake(incoming.getUserID(), incoming.getChunkRange());
      return;
    }

    for(Attribute attr: incoming.getAttributeList()){
      entity.updateAttribute(attr);
    }

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(
            incoming.getAttributeList(), incoming.getChunkRange(), entity.getUuid());

    for (UserID userID : activeChunkManager.getChunkRangeUsers(outgoing.getChunkRange())) {
      if (userID.equals(incoming.getUserID())) continue;
      serverNetworkHandle.send(userID, outgoing.toNetworkEvent());
    }
  }
}

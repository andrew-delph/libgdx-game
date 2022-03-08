package networking.events.consumer.server.incoming;

import app.GameController;
import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.SerializationDataMissing;
import entity.ActiveEntityManager;
import entity.Entity;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataDeserializer;

import java.util.function.Consumer;

public class CreateEntityIncomingConsumerServer implements Consumer<EventType> {

  @Inject GameController gameController;
  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ActiveChunkManager activeChunkManager;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType incoming = (CreateEntityIncomingEventType) eventType;
    Entity entity;
    try {
      entity =
          gameController.triggerAddEntity(
              entitySerializationConverter.createEntity(incoming.getData()));
    } catch (SerializationDataMissing e) {
      e.printStackTrace();
      return;
    }

    if (entity.getClass() == Entity.class) {
      activeEntityManager.registerActiveEntity(incoming.getUserID(), entity.uuid);
    }

    CreateEntityOutgoingEventType outgoing =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            incoming.getData(), incoming.getChunkRange());

    for (UserID userID : activeChunkManager.getChunkRangeUsers(incoming.getChunkRange())) {
      if (userID.equals(incoming.getUserID())) continue;
      serverNetworkHandle.send(userID, outgoing.toNetworkEvent());
    }
  }
}

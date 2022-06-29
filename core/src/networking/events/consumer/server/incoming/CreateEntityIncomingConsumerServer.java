package networking.events.consumer.server.incoming;

import app.game.GameController;
import app.user.UserID;
import chunk.ActiveChunkManager;
import com.google.inject.Inject;
import common.events.types.EventType;
import common.exceptions.ChunkNotFound;
import common.exceptions.SerializationDataMissing;
import entity.ActiveEntityManager;
import entity.Entity;
import entity.controllers.factories.EntityControllerFactory;
import entity.groups.Group;
import entity.groups.GroupService;
import java.util.function.Consumer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataDeserializer;

public class CreateEntityIncomingConsumerServer implements Consumer<EventType> {

  @Inject GameController gameController;
  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ActiveChunkManager activeChunkManager;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject GroupService groupService;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType incoming = (CreateEntityIncomingEventType) eventType;
    Entity entity;
    try {
      entity =
          gameController.triggerAddEntity(
              entitySerializationConverter.createEntity(incoming.getData()));
      entity.setEntityController(entityControllerFactory.createRemoteBodyController(entity));
      groupService.registerEntityGroup(entity.getUuid(), Group.PLAYER_GROUP);
    } catch (SerializationDataMissing | ChunkNotFound e) {
      e.printStackTrace();
      return;
    }

    if (entity.getClass() == Entity.class) {
      activeEntityManager.registerActiveEntity(incoming.getUserID(), entity.getUuid());
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

package networking.events.consumer.server.incoming;

import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventService;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.ActiveEntityManager;
import entity.Entity;
import networking.ConnectionStore;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.DisconnectionIncomingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DisconnectionIncomingConsumerServer implements Consumer<EventType> {

  private static final Logger LOGGER = Logger.getLogger(GameStore.class.getName());
  @Inject EventService eventService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventTypeFactory eventTypeFactory;
  @Inject ConnectionStore connectionStore;
  @Inject ActiveChunkManager activeChunkManager;
  @Inject ActiveEntityManager activeEntityManager;

  @Override
  public void accept(EventType eventType) {
    DisconnectionIncomingEventType realEvent = (DisconnectionIncomingEventType) eventType;
    connectionStore.removeConnection(realEvent.getUserID());
    activeChunkManager.removeUser(realEvent.getUserID());
    for (UUID ownersEntityUuid :
        activeEntityManager.getUserActiveEntitySet(realEvent.getUserID())) {
      Entity entity;
      try {
        entity = this.gameStore.getEntity(ownersEntityUuid);
      } catch (EntityNotFound e) {
        LOGGER.severe("DISCONNECT COULD NOT FIND AN ENTITY IT OWNS");
        e.printStackTrace();
        continue;
      }
      this.eventService.queuePostUpdateEvent(
          eventTypeFactory.createRemoveEntityEvent(ownersEntityUuid));

      RemoveEntityOutgoingEventType removeEntityOutgoingEvent =
          EventTypeFactory.createRemoveEntityOutgoingEvent(
              entity.uuid, new ChunkRange(entity.coordinates));

      for (UserID subscriptionUserID :
          activeChunkManager.getChunkRangeUsers(new ChunkRange(entity.coordinates))) {
        serverNetworkHandle.send(subscriptionUserID, removeEntityOutgoingEvent.toNetworkEvent());
      }
    }
  }
}

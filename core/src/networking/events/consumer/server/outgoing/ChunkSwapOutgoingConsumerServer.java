package networking.events.consumer.server.outgoing;

import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkSwapOutgoingConsumerServer implements Consumer<EventType> {

  final Logger LOGGER = LogManager.getLogger();
  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;

  @Override
  public void accept(EventType eventType) {
    /*
     Happens when an entity is chunk swapped on the server.
     The client needs to remove the entity if they don't have the chunk that it is swapped to.
     If the client doesn't have the chunk the entity is swapped into. It needs to create that entity.

     userIDSFrom are the users which don't have the chunks moved to
     userIDSTo are the users which don't have the chunk moved from

    */
    ChunkSwapOutgoingEventType chunkSwapOutgoingEventType = (ChunkSwapOutgoingEventType) eventType;
    NetworkObjects.NetworkEvent chunkSwapOutgoingNetworkEvent =
        chunkSwapOutgoingEventType.toNetworkEvent();

    Set<UserID> userIDSFromOriginal =
        activeChunkManager.getChunkRangeUsers(chunkSwapOutgoingEventType.getFrom());
    Set<UserID> userIDSToOriginal =
        activeChunkManager.getChunkRangeUsers(chunkSwapOutgoingEventType.getTo());

    Set<UserID> userIDSFrom = (new HashSet<>(userIDSFromOriginal));
    userIDSFrom.removeAll(userIDSToOriginal);

    Set<UserID> userIDSTo = (new HashSet<>(userIDSToOriginal));
    userIDSTo.removeAll(userIDSFromOriginal);

    for (UserID userID : userIDSFrom) {
      serverNetworkHandle.send(userID, chunkSwapOutgoingNetworkEvent);
    }

    Entity entityToCreate;
    try {
      entityToCreate = gameStore.getEntity(chunkSwapOutgoingEventType.getTarget());
    } catch (EntityNotFound e) {
      LOGGER.error("Unable to fetch entity for creation in chunk swap outgoing");
      return;
    }

    CreateEntityOutgoingEventType createEntityOutgoingEventType =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entityToCreate.toNetworkData(), new ChunkRange(entityToCreate.coordinates));

    NetworkObjects.NetworkEvent createEntityOutgoingNetworkEvent =
        createEntityOutgoingEventType.toNetworkEvent();

    for (UserID userID : userIDSTo) {
      serverNetworkHandle.send(userID, createEntityOutgoingNetworkEvent);
    }
  }
}

package networking.events.consumer.server.outgoing;

import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import common.events.types.EventType;
import common.exceptions.EntityNotFound;
import entity.Entity;
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
    ChunkSwapOutgoingEventType chunkSwapOutgoingEventType = (ChunkSwapOutgoingEventType) eventType;
    NetworkObjects.NetworkEvent chunkSwapOutgoingNetworkEvent =
        chunkSwapOutgoingEventType.toNetworkEvent();

    Set<UserID> userIDSFrom =
        activeChunkManager.getChunkRangeUsers(chunkSwapOutgoingEventType.getFrom());
    Set<UserID> userIDSTo =
        activeChunkManager.getChunkRangeUsers(chunkSwapOutgoingEventType.getTo());
    userIDSFrom.removeAll(userIDSTo);
    userIDSTo.removeAll(userIDSFrom);

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

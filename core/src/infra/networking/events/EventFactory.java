package infra.networking.events;

import infra.chunk.ChunkRange;
import infra.networking.NetworkObjects;

import java.util.List;

public interface EventFactory {
  CreateEntityOutgoingEvent createCreateEntityOutgoingEvent(NetworkObjects.NetworkData entityData);

  CreateEntityIncomingEvent createCreateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityOutgoingEvent createUpdateEntityOutgoingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityIncomingEvent createUpdateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  SubscriptionEvent createSubscriptionEvent(NetworkObjects.NetworkData entityData);

  SubscriptionEvent createSubscriptionEvent(List<ChunkRange> chunkRangeList);
}

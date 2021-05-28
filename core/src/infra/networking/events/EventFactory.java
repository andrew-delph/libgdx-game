package infra.networking.events;

import infra.chunk.ChunkRange;
import infra.networking.NetworkObjects;

import java.util.List;

public interface EventFactory {
  CreateEntityOutgoingEvent createCreateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange);

  CreateEntityIncomingEvent createCreateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityOutgoingEvent createUpdateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange);

  UpdateEntityIncomingEvent createUpdateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  SubscriptionOutgoingEvent createSubscriptionOutgoingEvent(List<ChunkRange> chunkRangeList);

  SubscriptionIncomingEvent createSubscriptionIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent);
}

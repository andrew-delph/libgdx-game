package infra.networking.events;

import infra.chunk.ChunkRange;
import infra.networking.NetworkObjects;

import java.util.List;
import java.util.UUID;

public interface EventFactory {
  CreateEntityOutgoingEvent createCreateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange);

  CreateEntityIncomingEvent createCreateEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent);

  UpdateEntityOutgoingEvent createUpdateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange);

  UpdateEntityIncomingEvent createUpdateEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent);

  SubscriptionOutgoingEvent createSubscriptionOutgoingEvent(List<ChunkRange> chunkRangeList);

  SubscriptionIncomingEvent createSubscriptionIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent);

  RemoveEntityIncomingEvent createRemoveEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent);

  DisconnectionEvent createDisconnectionEvent(UUID uuid);
}

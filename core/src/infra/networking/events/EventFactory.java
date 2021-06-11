package infra.networking.events;

import infra.chunk.ChunkRange;
import infra.common.events.RemoveEntityEvent;
import infra.common.events.ReplaceBlockEvent;
import infra.entity.block.Block;
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

  RemoveEntityOutgoingEvent createRemoveEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange);

  DisconnectionEvent createDisconnectionEvent(UUID uuid);

  ReplaceBlockIncomingEvent createReplaceBlockIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent);

  ReplaceBlockOutgoingEvent createReplaceBlockOutgoingEvent(
      UUID target, Block replacementBlock, ChunkRange chunkRange);

  ReplaceBlockEvent createReplaceBlockEvent(UUID target, Block replacementBlock);

  RemoveEntityEvent createRemoveEntityEvent(UUID entityUuid);
}

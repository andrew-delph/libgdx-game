package networking.events;

import com.google.inject.Inject;
import chunk.ChunkRange;
import common.events.RemoveEntityEvent;
import common.events.ReplaceBlockEvent;
import entity.block.Block;
import networking.NetworkObjects;

import java.util.List;
import java.util.UUID;

public class EventFactory {

  @Inject
  EventFactory() {}

  public CreateEntityOutgoingEvent createCreateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    return new CreateEntityOutgoingEvent(entityData, chunkRange);
  }
  ;

  public CreateEntityIncomingEvent createCreateEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent) {
    return new CreateEntityIncomingEvent(networkEvent);
  }
  ;

  public UpdateEntityOutgoingEvent createUpdateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    return new UpdateEntityOutgoingEvent(entityData, chunkRange);
  }
  ;

  public UpdateEntityIncomingEvent createUpdateEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent) {
    return new UpdateEntityIncomingEvent(networkEvent);
  }
  ;

  public SubscriptionOutgoingEvent createSubscriptionOutgoingEvent(
      List<ChunkRange> chunkRangeList) {
    return new SubscriptionOutgoingEvent(chunkRangeList);
  }
  ;

  public SubscriptionIncomingEvent createSubscriptionIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent) {
    return new SubscriptionIncomingEvent(networkEvent);
  }
  ;

  public RemoveEntityIncomingEvent createRemoveEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent) {
    return new RemoveEntityIncomingEvent(networkEvent);
  }
  ;

  public RemoveEntityOutgoingEvent createRemoveEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
    return new RemoveEntityOutgoingEvent(entityData, chunkRange);
  }
  ;

  public DisconnectionEvent createDisconnectionEvent(UUID uuid) {
    return new DisconnectionEvent(uuid);
  }
  ;

  public ReplaceBlockIncomingEvent createReplaceBlockIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent) {
    return new ReplaceBlockIncomingEvent(networkEvent);
  }
  ;

  public ReplaceBlockOutgoingEvent createReplaceBlockOutgoingEvent(
      UUID target, Block replacementBlock, ChunkRange chunkRange) {
    return new ReplaceBlockOutgoingEvent(target, replacementBlock, chunkRange);
  }
  ;

  public ReplaceBlockEvent createReplaceBlockEvent(UUID target, Block replacementBlock) {
    return new ReplaceBlockEvent(target, replacementBlock);
  }
  ;

  public RemoveEntityEvent createRemoveEntityEvent(UUID entityUuid) {
    return new RemoveEntityEvent(entityUuid);
  }
  ;
}

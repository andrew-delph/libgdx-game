package networking.events.types.outgoing;

import static networking.events.types.NetworkEventTypeEnum.REPLACE_ENTITY_OUTGOING;

import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.types.EventType;
import entity.Entity;
import java.util.UUID;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;
import networking.translation.NetworkDataSerializer;

public class ReplaceBlockOutgoingEventType extends EventType implements SerializeNetworkEvent {

  public static String type = REPLACE_ENTITY_OUTGOING;

  UUID target;
  Entity replacementEntity;
  ChunkRange chunkRange;

  @Inject
  public ReplaceBlockOutgoingEventType(
      UUID target, Entity replacementEntity, ChunkRange chunkRange) {
    this.target = target;
    this.replacementEntity = replacementEntity;
    this.chunkRange = chunkRange;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public UUID getTarget() {
    return target;
  }

  public Entity getReplacementEntity() {
    return replacementEntity;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkDataSerializer.createReplaceBlockOutgoingEventType(this);
  }
}

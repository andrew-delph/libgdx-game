package core.networking.events.types.outgoing;

import core.chunk.ChunkRange;
import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.translation.NetworkDataSerializer;
import core.entity.Entity;
import java.util.UUID;
import networking.NetworkObjects;
import core.networking.events.interfaces.SerializeNetworkEvent;

public class ReplaceBlockOutgoingEventType extends EventType implements SerializeNetworkEvent {

  public static String type = NetworkEventTypeEnum.REPLACE_ENTITY_OUTGOING;

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
  public String getEventType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkDataSerializer.createReplaceBlockOutgoingEventType(this);
  }
}

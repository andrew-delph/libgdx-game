package core.networking.events.types.outgoing;

import core.chunk.ChunkRange;
import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.translation.NetworkDataSerializer;
import java.util.UUID;
import networking.NetworkObjects;
import core.networking.events.interfaces.SerializeNetworkEvent;

public class RemoveEntityOutgoingEventType extends EventType implements SerializeNetworkEvent {
  public static String type = NetworkEventTypeEnum.REMOVE_ENTITY_OUTGOING;

  UUID target;
  ChunkRange chunkRange;

  @Inject
  public RemoveEntityOutgoingEventType(UUID target, ChunkRange chunkRange) {
    this.target = target;
    this.chunkRange = chunkRange;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public UUID getTarget() {
    return target;
  }

  @Override
  public String getEventType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkDataSerializer.createRemoveEntityOutgoingEventType(this);
  }
}

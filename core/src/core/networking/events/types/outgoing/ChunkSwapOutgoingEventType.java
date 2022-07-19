package core.networking.events.types.outgoing;

import static core.networking.events.types.NetworkEventTypeEnum.CHUNK_SWAP_OUTGOING;
import static core.networking.translation.NetworkDataSerializer.createChunkSwapOutgoingEventType;

import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import java.util.UUID;
import networking.NetworkObjects;
import core.networking.events.interfaces.SerializeNetworkEvent;

public class ChunkSwapOutgoingEventType extends EventType implements SerializeNetworkEvent {

  UUID target;
  ChunkRange from;
  ChunkRange to;

  public ChunkSwapOutgoingEventType(UUID target, ChunkRange from, ChunkRange to) {
    this.target = target;
    this.from = from;
    this.to = to;
  }

  public UUID getTarget() {
    return target;
  }

  public ChunkRange getFrom() {
    return from;
  }

  public ChunkRange getTo() {
    return to;
  }

  @Override
  public String getEventType() {
    return CHUNK_SWAP_OUTGOING;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return createChunkSwapOutgoingEventType(this);
  }
}

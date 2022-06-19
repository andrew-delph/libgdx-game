package networking.events.types.incoming;

import static networking.events.types.NetworkEventTypeEnum.CHUNK_SWAP_INCOMING;

import chunk.ChunkRange;
import common.events.types.EventType;
import java.util.UUID;

public class ChunkSwapIncomingEventType extends EventType {

  UUID target;
  ChunkRange from;
  ChunkRange to;

  public ChunkSwapIncomingEventType(UUID target, ChunkRange from, ChunkRange to) {
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
    return CHUNK_SWAP_INCOMING;
  }
}

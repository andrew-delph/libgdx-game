package networking.events.types.incoming;

import chunk.ChunkRange;
import common.events.types.EventType;

import java.util.UUID;

import static networking.events.types.NetworkEventTypeEnum.CHUNK_SWAP_INCOMING;

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
  public String getType() {
    return CHUNK_SWAP_INCOMING;
  }
}

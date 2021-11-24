package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

public class GetChunkOutgoingEventType extends EventType implements SerializeNetworkEvent {
  static String type = "get_chunk";

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  ChunkRange chunkRange;

  public GetChunkOutgoingEventType(ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder()
        .setData(this.chunkRange.toNetworkData())
        .setEvent(type)
        .build();
  }
}

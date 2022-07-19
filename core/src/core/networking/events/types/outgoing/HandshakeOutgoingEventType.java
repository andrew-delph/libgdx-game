package core.networking.events.types.outgoing;

import core.chunk.ChunkRange;
import core.common.events.types.EventType;
import core.networking.events.interfaces.SerializeNetworkEvent;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.translation.DataTranslationEnum;
import core.networking.translation.NetworkDataSerializer;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;

public class HandshakeOutgoingEventType extends EventType implements SerializeNetworkEvent {

  ChunkRange chunkRange;
  List<UUID> listUUID;

  public HandshakeOutgoingEventType(ChunkRange chunkRange, List<UUID> listUUID) {
    this.chunkRange = chunkRange;
    this.listUUID = listUUID;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
  }

  public List<UUID> getListUUID() {
    return listUUID;
  }

  @Override
  public String getEventType() {
    return NetworkEventTypeEnum.HANDSHAKE_OUTGOING;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    NetworkObjects.NetworkData data =
        NetworkObjects.NetworkData.newBuilder()
            .addChildren(this.chunkRange.toNetworkData())
            .addChildren(NetworkDataSerializer.createUUIDList(this.listUUID))
            .build();
    return NetworkObjects.NetworkEvent.newBuilder()
        .setEvent(DataTranslationEnum.HANDSHAKE)
        .setData(data)
        .build();
  }
}

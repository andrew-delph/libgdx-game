package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;
import networking.translation.NetworkDataSerializer;

import java.util.List;
import java.util.UUID;

public class HandshakeOutgoingEventType extends EventType implements SerializeNetworkEvent {

    ChunkRange chunkRange;
    List<UUID> listUUID;

    public HandshakeOutgoingEventType(ChunkRange chunkRange, List<UUID> listUUID) {
        this.chunkRange = chunkRange;
        this.listUUID = listUUID;
    }

    @Override
    public String getType() {
        return "handshake_outgoing";
    }

    @Override
    public NetworkObjects.NetworkEvent toNetworkEvent() {
        NetworkObjects.NetworkData data = NetworkObjects.NetworkData.newBuilder()
                .addChildren(this.chunkRange.toNetworkData())
                .addChildren(NetworkDataSerializer.createUUIDList(this.listUUID))
                .build();
        return NetworkObjects.NetworkEvent.newBuilder().setEvent(this.getType()).setData(data).build();
    }
}

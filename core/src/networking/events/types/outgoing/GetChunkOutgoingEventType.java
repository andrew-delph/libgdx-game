package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

import java.util.UUID;

public class GetChunkOutgoingEventType extends EventType implements SerializeNetworkEvent {
    static String type = "get_chunk";
    ChunkRange chunkRange;
    UUID userId;

    public GetChunkOutgoingEventType(ChunkRange chunkRange, UUID userId) {
        this.chunkRange = chunkRange;
        this.userId = userId;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    @Override
    public String getType() {
        return type;
    }

    public UUID getUUID() {
        return this.userId;
    }

    @Override
    public NetworkObjects.NetworkEvent toNetworkEvent() {
        return NetworkObjects.NetworkEvent.newBuilder()
                .setData(this.chunkRange.toNetworkData())
                .setUser(this.userId.toString())
                .setEvent(type)
                .build();
    }
}

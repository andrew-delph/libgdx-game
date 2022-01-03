package networking.events.types.incoming;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.events.types.NetworkEventTypeEnum;

import java.util.List;
import java.util.UUID;

public class HandshakeIncomingEventType extends EventType {

    ChunkRange chunkRange;
    List<UUID> listUUID;
    UUID requestUUID;

    public HandshakeIncomingEventType(UUID requestUUID, ChunkRange chunkRange, List<UUID> listUUID) {
        this.chunkRange = chunkRange;
        this.listUUID = listUUID;
        this.requestUUID = requestUUID;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    public List<UUID> getListUUID() {
        return listUUID;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    @Override
    public String getType() {
        return NetworkEventTypeEnum.HANDSHAKE_INCOMING;
    }
}

package networking.events.types.incoming;

import chunk.ChunkRange;
import common.events.types.EventType;

import java.util.List;
import java.util.UUID;

public class HandshakeIncomingEventType extends EventType {

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    public List<UUID> getListUUID() {
        return listUUID;
    }

    ChunkRange chunkRange;
    List<UUID> listUUID;

    public HandshakeIncomingEventType(ChunkRange chunkRange, List<UUID> listUUID) {
        this.chunkRange = chunkRange;
        this.listUUID = listUUID;
    }

    @Override
    public String getType() {
        return "handshake_incoming";
    }
}

package networking.events.types.incoming;

import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;

import java.util.UUID;

import static networking.events.types.NetworkEventTypeEnum.UPDATE_ENTITY_INCOMING;

public class UpdateEntityIncomingEventType extends EventType {

    public static String type = UPDATE_ENTITY_INCOMING;

    UUID user;
    NetworkObjects.NetworkData networkData;
    ChunkRange chunkRange;

    @Inject
    public UpdateEntityIncomingEventType(UUID user, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        this.networkData = networkData;
        this.chunkRange = chunkRange;
    }

    public NetworkObjects.NetworkData getData() {
        return this.networkData;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    public UUID getUser() {
        return this.user;
    }

    @Override
    public String getType() {
        return type;
    }
}

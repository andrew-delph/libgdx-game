package networking.events.types.incoming;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.types.NetworkEventTypeEnum;

import java.util.UUID;

public class CreateEntityIncomingEventType extends EventType {

    public static String type = NetworkEventTypeEnum.CREATE_ENTITY_INCOMING;


    UUID user;
    NetworkObjects.NetworkData networkData;
    ChunkRange chunkRange;

    public CreateEntityIncomingEventType(UUID user, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        this.user = user;
        this.networkData = networkData;
        this.chunkRange = chunkRange;
    }

    public NetworkObjects.NetworkData getData() {
        return networkData;
    }

    public UUID getUser() {
        return this.user;
    }
    

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    @Override
    public String getType() {
        return type;
    }
}

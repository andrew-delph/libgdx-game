package networking.events.types.incoming;

import chunk.ChunkRange;
import common.events.types.EventType;

import java.util.UUID;

import static networking.events.types.NetworkEventTypeEnum.REMOVE_ENTITY_INCOMING;

public class RemoveEntityIncomingEventType extends EventType {
    public static String type = REMOVE_ENTITY_INCOMING;

    ChunkRange chunkRange;
    UUID user;
    UUID target;

    public RemoveEntityIncomingEventType(UUID user, ChunkRange chunkRange, UUID target) {
        this.chunkRange = chunkRange;
        this.user = user;
        this.target = target;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    public UUID getUser() {
        return user;
    }

    public UUID getTarget() {
        return target;
    }

    @Override
    public String getType() {
        return type;
    }
}

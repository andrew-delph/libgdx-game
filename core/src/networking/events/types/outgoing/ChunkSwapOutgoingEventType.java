package networking.events.types.outgoing;

import chunk.ChunkRange;
import common.events.types.EventType;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

import java.util.UUID;

import static networking.events.types.NetworkEventTypeEnum.CHUNK_SWAP_OUTGOING;
import static networking.translation.NetworkDataSerializer.createChunkSwapOutgoingEventType;

public class ChunkSwapOutgoingEventType  extends EventType implements SerializeNetworkEvent {

    UUID target;
    ChunkRange from;
    ChunkRange to;

    public ChunkSwapOutgoingEventType(UUID target, ChunkRange from, ChunkRange to) {
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
        return CHUNK_SWAP_OUTGOING;
    }

    @Override
    public NetworkObjects.NetworkEvent toNetworkEvent() {
        return createChunkSwapOutgoingEventType(this);
    }
}

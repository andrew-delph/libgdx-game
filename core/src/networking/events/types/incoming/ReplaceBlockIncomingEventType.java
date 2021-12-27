package networking.events.types.incoming;

import chunk.ChunkRange;
import common.events.types.EventType;
import entity.block.Block;

import java.util.UUID;

import static networking.events.types.NetworkEventTypeEnum.REPLACE_ENTITY_INCOMING;

public class ReplaceBlockIncomingEventType extends EventType {

    public static String type = REPLACE_ENTITY_INCOMING;

    UUID user;
    UUID target;
    Block replacementBlock;
    ChunkRange chunkRange;

    public ReplaceBlockIncomingEventType(UUID user, UUID target, Block replacementBlock, ChunkRange chunkRange) {
        this.user = user;
        this.target = target;
        this.replacementBlock = replacementBlock;
        this.chunkRange = chunkRange;
    }

    public UUID getTarget() {
        return target;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    public Block getReplacementBlock() {
        return replacementBlock;
    }


    public UUID getUser() {
        return this.user;
    }

    @Override
    public String getType() {
        return type;
    }
}

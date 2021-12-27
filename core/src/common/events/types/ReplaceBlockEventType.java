package common.events.types;

import chunk.ChunkRange;
import com.google.inject.Inject;
import entity.block.Block;

import java.util.UUID;

public class ReplaceBlockEventType extends EventType {

    public static String type = "replace_block";

    UUID target;
    Block replacementBlock;
    ChunkRange chunkRange;

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    @Inject
    public ReplaceBlockEventType(UUID target, Block replacementBlock, ChunkRange chunkRange) {
        this.target = target;
        this.replacementBlock = replacementBlock;
        this.chunkRange = chunkRange;
    }

    public UUID getTarget() {
        return target;
    }

    public Block getReplacementBlock() {
        return replacementBlock;
    }

    @Override
    public String getType() {
        return type;
    }
}

package common.events.types;

import chunk.ChunkRange;
import com.google.inject.Inject;
import entity.Entity;
import entity.block.Block;

import java.util.UUID;

public class ReplaceEntityEventType extends EventType {

    public static String type = "replace_block";

    UUID target;
    Entity replacementEntity;
    ChunkRange chunkRange;

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    @Inject
    public ReplaceEntityEventType(UUID target, Entity replacementBlock, ChunkRange chunkRange) {
        this.target = target;
        this.replacementEntity = replacementBlock;
        this.chunkRange = chunkRange;
    }

    public UUID getTarget() {
        return target;
    }

    public Entity getReplacementEntity() {
        return replacementEntity;
    }

    @Override
    public String getType() {
        return type;
    }
}

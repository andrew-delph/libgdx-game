package common.events.types;

import chunk.ChunkRange;
import entity.Entity;

import java.util.UUID;

public class ReplaceEntityEventType extends EventType {

    public static String type = "replace_block";

    UUID target;
    Entity replacementEntity;
    ChunkRange chunkRange;
    Boolean swapVelocity;


    public ReplaceEntityEventType(UUID target, Entity replacementBlock, Boolean swapVelocity, ChunkRange chunkRange) {
        this.target = target;
        this.replacementEntity = replacementBlock;
        this.chunkRange = chunkRange;
        this.swapVelocity = swapVelocity;
    }

    public Boolean getSwapVelocity() {
        return swapVelocity;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
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

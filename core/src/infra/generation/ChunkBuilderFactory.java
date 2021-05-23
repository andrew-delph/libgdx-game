package infra.generation;

import infra.chunk.ChunkRange;

public interface ChunkBuilderFactory {
    ChunkBuilder create(ChunkRange chunkRange);
}

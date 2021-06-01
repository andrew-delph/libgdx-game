package infra.chunk;

import com.google.inject.assistedinject.Assisted;

public interface ChunkFactory {
  Chunk create(@Assisted ChunkRange chunkRange);
}

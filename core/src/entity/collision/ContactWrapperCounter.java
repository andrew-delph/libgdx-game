package entity.collision;

import chunk.ChunkRange;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.UUID;

public abstract class ContactWrapperCounter {
  Table<UUID, ChunkRange, Integer> contactCounterTable = HashBasedTable.create();

  public synchronized void beginContact(CollisionPoint source, CollisionPoint target) {
    if (!contactCounterTable.contains(source.getUuid(), source.getChunkRange())) {
      contactCounterTable.put(source.getUuid(), source.getChunkRange(), 0);
    }
    if (!contactCounterTable.contains(target.getUuid(), target.getChunkRange())) {
      contactCounterTable.put(target.getUuid(), target.getChunkRange(), 0);
    }

    contactCounterTable.put(
        source.getUuid(),
        source.getChunkRange(),
        contactCounterTable.get(source.getUuid(), source.getChunkRange()) + 1);

    contactCounterTable.put(
        target.getUuid(),
        target.getChunkRange(),
        contactCounterTable.get(target.getUuid(), target.getChunkRange()) + 1);
  }

  public synchronized void endContact(CollisionPoint source, CollisionPoint target) {
    if (contactCounterTable.contains(source.getUuid(), source.getChunkRange())) {
      contactCounterTable.put(
          source.getUuid(),
          source.getChunkRange(),
          contactCounterTable.get(source.getUuid(), source.getChunkRange()) - 1);
    }
    if (contactCounterTable.contains(target.getUuid(), target.getChunkRange())) {
      contactCounterTable.put(
          target.getUuid(),
          target.getChunkRange(),
          contactCounterTable.get(target.getUuid(), target.getChunkRange()) - 1);
    }
  }

  public Integer getContactCount(UUID uuid, ChunkRange chunkRange) {
    return contactCounterTable.get(uuid, chunkRange);
  }
}

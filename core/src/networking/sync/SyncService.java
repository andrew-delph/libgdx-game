package networking.sync;

import app.user.UserID;
import chunk.ChunkRange;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import common.Clock;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SyncService {
  /** overloading can happen on the client or the server if too many handshake requests happen */
  @Inject Clock clock;

  Map<UserID, Set<ChunkRange>> handshakeLock = new ConcurrentHashMap<>();

  public synchronized Boolean lockHandshake(
      UserID userID, ChunkRange chunkRange, int existsForTicks) {
    if (this.isHandshakeLocked(userID, chunkRange)) return false;
    handshakeLock.putIfAbsent(userID, Sets.newConcurrentHashSet());
    handshakeLock.get(userID).add(chunkRange);
    clock.addTaskOnTick(
        existsForTicks,
        () -> {
          this.removeHandshakeLock(userID, chunkRange);
        });
    return true;
  }

  private synchronized void removeHandshakeLock(UserID userID, ChunkRange chunkRange) {
    handshakeLock.putIfAbsent(userID, Sets.newConcurrentHashSet());
    handshakeLock.get(userID).remove(chunkRange);
  }

  public synchronized Boolean isHandshakeLocked(UserID userID, ChunkRange chunkRange) {
    handshakeLock.putIfAbsent(userID, Sets.newConcurrentHashSet());
    return handshakeLock.get(userID).contains(chunkRange);
  }
}

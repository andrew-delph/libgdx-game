package chunk;

import app.user.UserID;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveChunkManager {

  private final ConcurrentHashMap<UserID, Set<ChunkRange>> userIDChunkRange =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<ChunkRange, Set<UserID>> chunkRangeUserID =
      new ConcurrentHashMap<>();

  public synchronized void setUserChunkSubscriptions(
      UserID userID, Collection<ChunkRange> chunkRangeList) {
    // remove a user
    // add all the edges
    this.removeUser(userID);
    for (ChunkRange chunkRange : chunkRangeList) {
      this.addUserChunkSubscriptions(userID, chunkRange);
    }
  }

  public void addUserChunkSubscriptions(UserID userID, ChunkRange chunkRange) {
    userIDChunkRange.putIfAbsent(userID, new HashSet<>());
    chunkRangeUserID.putIfAbsent(chunkRange, new HashSet<>());

    userIDChunkRange.get(userID).add(chunkRange);
    chunkRangeUserID.get(chunkRange).add(userID);
  }

  public synchronized void removeUser(UserID userID) {
    Set<ChunkRange> chunkRangeSet = this.getUserChunkRanges(userID);
    for (ChunkRange chunkRange : chunkRangeSet) {
      chunkRangeUserID.get(chunkRange).remove(userID);
    }
    userIDChunkRange.remove(userID);
  }

  public Set<ChunkRange> getUserChunkRanges(UserID userID) {
    return new HashSet<>(userIDChunkRange.getOrDefault(userID, new HashSet<>()));
  }

  public Set<UserID> getChunkRangeUsers(ChunkRange chunkRange) {
    return new HashSet<>(chunkRangeUserID.getOrDefault(chunkRange, new HashSet<>()));
  }
}

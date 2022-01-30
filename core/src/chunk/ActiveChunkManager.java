package chunk;

import app.user.UserID;

import java.util.*;

public class ActiveChunkManager {

    private final Map<UserID, Set<ChunkRange>> userIDChunkRange = new HashMap<>();
    private final Map<ChunkRange, Set<UserID>> chunkRangeUserID = new HashMap<>();

    public synchronized void setUserChunkSubscriptions(UserID userID, Collection<ChunkRange> chunkRangeList) {
        // remove a user
        // add all the edges
        this.removeUser(userID);
        for (ChunkRange chunkRange : chunkRangeList) {
            this.addUserChunkSubscriptions(userID, chunkRange);
        }
    }

    public synchronized void addUserChunkSubscriptions(UserID userID, ChunkRange chunkRange) {
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
        return userIDChunkRange.getOrDefault(userID, new HashSet<>());
    }

    public Set<UserID> getChunkRangeUsers(ChunkRange chunkRange) {
        return chunkRangeUserID.getOrDefault(chunkRange, new HashSet<>());
    }
}

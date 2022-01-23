package chunk;

import app.user.UserID;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.*;

public class ActiveChunkManager {

    Map<UserID, Set<ChunkRange>> userIDChunkRange = new HashMap<>();
    Map<ChunkRange, Set<UserID>> chunkRangeUserID = new HashMap<>();

    public synchronized void setUserChunkSubscriptions(UserID userID, Collection<ChunkRange> chunkRangeList) {
        // remove a user
        // add all the edges
        this.removeUser(userID);
        for(ChunkRange chunkRange: chunkRangeList){
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
        for(ChunkRange chunkRange: chunkRangeSet){
            chunkRangeUserID.get(chunkRange).remove(userID);
        }
        userIDChunkRange.remove(userID);
    }

    public Set<ChunkRange> getUserChunkRanges(UserID userID) {
        return userIDChunkRange.get(userID);
    }

    public Set<UserID> getChunkRangeUsers(ChunkRange chunkRange) {
        return chunkRangeUserID.get(chunkRange);
    }
}

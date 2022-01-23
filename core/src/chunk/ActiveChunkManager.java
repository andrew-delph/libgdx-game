package chunk;

import app.user.UserID;

import java.util.*;

public class ActiveChunkManager {

    Map<UserID, Set<ChunkRange>> userIDChunkRangeMap = new HashMap<>();

    public void registerChunkSubscriptions(UserID user_uuid, Collection<ChunkRange> chunkRangeList) {
        userIDChunkRangeMap.putIfAbsent(user_uuid, new HashSet<>());
        userIDChunkRangeMap.get(user_uuid).addAll(chunkRangeList);
    }

    public Set<ChunkRange> getUserChunkSubscriptions(UserID user_uuid) {
        return userIDChunkRangeMap.get(user_uuid);
    }

    public void deregisterUser(UserID user_uuid) {
        userIDChunkRangeMap.remove(user_uuid);
    }
}

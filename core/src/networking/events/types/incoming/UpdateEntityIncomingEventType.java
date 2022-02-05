package networking.events.types.incoming;

import app.user.UserID;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;

import static networking.events.types.NetworkEventTypeEnum.UPDATE_ENTITY_INCOMING;

public class UpdateEntityIncomingEventType extends EventType {

    public static String type = UPDATE_ENTITY_INCOMING;

    UserID userID;
    NetworkObjects.NetworkData networkData;
    ChunkRange chunkRange;

    @Inject
    public UpdateEntityIncomingEventType(UserID userID, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        this.networkData = networkData;
        this.chunkRange = chunkRange;
        this.userID = userID;
    }

    public NetworkObjects.NetworkData getData() {
        return this.networkData;
    }

    public ChunkRange getChunkRange() {
        return chunkRange;
    }

    public UserID getUserID() {
        return this.userID;
    }

    @Override
    public String getType() {
        return type;
    }
}

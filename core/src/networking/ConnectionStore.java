package networking;

import chunk.ChunkSubscriptionManager;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionStore {

    Map<UUID, RequestNetworkEventObserver> connectionMap;

    @Inject
    ChunkSubscriptionManager chunkSubscriptionManager;

    @Inject
    public ConnectionStore() {
        this.connectionMap = new HashMap<>();
    }

    public void addConnection(UUID uuid, RequestNetworkEventObserver requestNetworkEventObserver) {
        this.connectionMap.put(uuid, requestNetworkEventObserver);
    }

    public void removeConnection(UUID uuid) {
        this.connectionMap.remove(uuid);
        chunkSubscriptionManager.removeUUID(uuid);
    }

    public RequestNetworkEventObserver getConnection(UUID uuid) {
        return this.connectionMap.get(uuid);
    }

    public int size() {
        return this.connectionMap.size();
    }
}

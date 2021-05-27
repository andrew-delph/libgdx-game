package infra.networking;

import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionStore {

  Map<UUID, RequestNetworkEventObserver> connectionMap;

  @Inject
  public ConnectionStore() {
    this.connectionMap = new HashMap<>();
  }

  public void addConnection(UUID uuid, RequestNetworkEventObserver requestNetworkEventObserver) {
    this.connectionMap.put(uuid, requestNetworkEventObserver);
  }

  public int size() {
    return this.connectionMap.size();
  }
}

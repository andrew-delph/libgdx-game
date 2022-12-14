package core.networking;

import com.google.inject.Inject;
import core.app.user.UserID;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStore {

  public Instant inactiveStartTime;
  Map<UserID, RequestNetworkEventObserver> connectionMap = new ConcurrentHashMap<>();

  @Inject
  public ConnectionStore() {
    inactiveStartTime = Instant.now();
  }

  public synchronized void addConnection(
      UserID userID, RequestNetworkEventObserver requestNetworkEventObserver) {
    this.connectionMap.put(userID, requestNetworkEventObserver);
    inactiveStartTime = null;
  }

  public synchronized void removeConnection(UserID userID) {
    this.connectionMap.remove(userID);
    if (connectionMap.size() == 0) inactiveStartTime = Instant.now();
  }

  public RequestNetworkEventObserver getConnection(UserID userID) {
    return this.connectionMap.get(userID);
  }

  public List<UserID> getConnectedUserID() {
    return new LinkedList<>(this.connectionMap.keySet());
  }

  public synchronized int size() {
    return this.connectionMap.size();
  }
}

package core.networking;

import com.google.inject.Inject;
import core.app.user.UserID;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStore {

  Map<UserID, RequestNetworkEventObserver> connectionMap = new ConcurrentHashMap<>();

  @Inject
  public ConnectionStore() {}

  public void addConnection(
      UserID userID, RequestNetworkEventObserver requestNetworkEventObserver) {
    this.connectionMap.put(userID, requestNetworkEventObserver);
  }

  public void removeConnection(UserID userID) {
    this.connectionMap.remove(userID);
  }

  public RequestNetworkEventObserver getConnection(UserID userID) {
    return this.connectionMap.get(userID);
  }

  public List<UserID> getConnectedUserID() {
    return new LinkedList<>(this.connectionMap.keySet());
  }

  public int size() {
    return this.connectionMap.size();
  }
}

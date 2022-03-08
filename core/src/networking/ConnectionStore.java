package networking;

import app.user.UserID;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ConnectionStore {

  Map<UserID, RequestNetworkEventObserver> connectionMap = new HashMap<>();

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

  public int size() {
    return this.connectionMap.size();
  }
}

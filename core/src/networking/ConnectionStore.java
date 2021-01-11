package networking;

import java.util.HashMap;

public class ConnectionStore {
    static ConnectionStore instance;

    public static ConnectionStore getInstance() {
        if (instance == null) {
            instance = new ConnectionStore();
        }
        return instance;
    }

    HashMap<String, Object> connections;

    ConnectionStore() {
        this.connections = new HashMap<>();
    }

    public Object[] getAll(HashMap<String, String> filter) {
        return this.connections.values().toArray(new Object[0]);
    }


}

package networking.server;

import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.HashMap;

public class ConnectionStore {
    static ConnectionStore instance;

    public static ConnectionStore getInstance() {
        if (instance == null) {
            instance = new ConnectionStore();
        }
        return instance;
    }

    HashMap<String, StreamObserver> connections;

    ConnectionStore() {
        this.connections = new HashMap<>();
    }

    public StreamObserver[] getAll(Class clazz) {
        StreamObserver[] values = this.connections.values().toArray(new StreamObserver[0]);
        if (clazz != null) {
            values = (StreamObserver[]) Arrays.stream(values).filter(o -> {
                return clazz.isInstance(o);
            }).map( StreamObserver.class::cast ).toArray();
        }
        return values;
    }


}

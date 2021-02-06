package networking.server;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionStore {
    static ConnectionStore instance;

    public static ConnectionStore getInstance() {
        if (instance == null) {
            instance = new ConnectionStore();
        }
        return instance;
    }

    List<StreamObserver> connections;

    ConnectionStore() {
        this.connections = new ArrayList();
    }

    public void add(StreamObserver observer) {
        connections.add(observer);
    }

    public <E extends StreamObserver> List<E> getAll(Class<E> clazz) {
        return this.connections.stream().filter(clazz::isInstance)
                .map(clazz::cast).collect(Collectors.toList());
    }
}

package networking.server.connetion;

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

    List<AbtractConnection> connections;

    ConnectionStore() {
        this.connections = new ArrayList();
    }

    public void add(AbtractConnection connection) {
        connections.add(connection);
    }

    public <E extends AbtractConnection> List<E> getAll(Class<E> clazz) {
        return this.connections.stream().filter(clazz::isInstance)
                .map(clazz::cast).collect(Collectors.toList());
    }
}

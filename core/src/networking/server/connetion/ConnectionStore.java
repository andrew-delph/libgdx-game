package networking.server.connetion;

import com.google.inject.Inject;
import common.interfaces.Cleanup;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionStore implements Cleanup {
    static ConnectionStore instance;

    List<AbtractConnection> connections;

    @Inject
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

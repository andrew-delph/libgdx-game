package networking.connetion;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ConnectionStore {
    static ConnectionStore instance;

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

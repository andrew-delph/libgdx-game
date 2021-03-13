package networking.connetion;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class ConnectionStore {

    Map<UUID, AbtractConnection> connections;

    ConnectionStore() {
        this.connections = new HashMap<>();
    }

    public void add(AbtractConnection connection) {
        connections.put(connection.id, connection);
    }

    public AbtractConnection get(UUID id) {
        return this.connections.get(id);
    }

    public void remove(UUID id) {
        this.connections.remove(id);
    }

    public <E extends AbtractConnection> List<E> getAll(Class<E> clazz) {
        return this.connections.values().stream().filter(clazz::isInstance)
                .map(clazz::cast).collect(Collectors.toList());
    }

}

package networking.connection;

import com.google.inject.Singleton;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class ConnectionStore {

  Map<UUID, AbtractConnection> connections;
  Map<StreamObserver, AbtractConnection> observersMap;

  ConnectionStore() {
    this.connections = new HashMap<>();
    this.observersMap = new HashMap<>();
  }

  public void add(AbtractConnection connection) {
    connections.put(connection.id, connection);
    observersMap.put(connection.responseObserver, connection);
    observersMap.put(connection.requestObserver, connection);
  }

  public AbtractConnection get(UUID id) {
    return this.connections.get(id);
  }

  public AbtractConnection get(StreamObserver observer) {
    return this.observersMap.get(observer);
  }

  public void remove(UUID id) {
    AbtractConnection connection = this.connections.get(id);
    this.connections.remove(id);
    this.observersMap.remove(connection.requestObserver);
    this.observersMap.remove(connection.responseObserver);
  }

  public <E extends AbtractConnection> List<E> getAll(Class<E> clazz) {
    return this.connections.values().stream()
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .collect(Collectors.toList());
  }
}

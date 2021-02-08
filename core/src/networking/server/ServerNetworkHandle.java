package networking.server;

import infra.entity.Entity;
import infra.entity.EntityManager;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;
import networking.server.connetion.ConnectionStore;
import networking.server.connetion.CreateConnection;
import networking.server.connetion.UpdateConnection;
import networking.server.observer.CreateObserver;
import networking.server.observer.RemoveObserver;
import networking.server.observer.UpdateObserver;

import java.io.IOException;
import java.util.UUID;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

    final public EntityManager entityManager;
    final public ConnectionStore connectionStore;
    final UUID id;
    final Server server;

    public ServerNetworkHandle() {
        this.id = UUID.randomUUID();
        entityManager = EntityManager.getInstance(this.id);
        server = ServerBuilder.forPort(99).addService(this).build();
        connectionStore = ConnectionStore.getInstance();
    }

    public void start() throws IOException {
        server.start();
    }

    public void close(){
        server.shutdown();
        // TODO check if server closed
    }

    @Override
    public StreamObserver<NetworkObject.CreateNetworkObject> create(StreamObserver<NetworkObject.CreateNetworkObject> responseObserver) {
        CreateObserver createObserver = new CreateObserver(this.id);
        connectionStore.add(new CreateConnection(responseObserver));
        for (Entity entity : this.entityManager.getAll()) {
            NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((entity.getEntityData().getX() + "")).build();
            NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((entity.getEntityData().getY() + "")).build();
            NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(entity.getEntityData().getID()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
            responseObserver.onNext(createRequestObject);
        }
        return createObserver;
    }

    @Override
    public StreamObserver<NetworkObject.UpdateNetworkObject> update(StreamObserver<NetworkObject.UpdateNetworkObject> responseObserver) {
        UpdateObserver updateObserver = new UpdateObserver(this.id);
        connectionStore.add(new UpdateConnection(responseObserver));
        return updateObserver;
    }

    @Override
    public StreamObserver<NetworkObject.RemoveNetworkObject> remove(StreamObserver<NetworkObject.RemoveNetworkObject> responseObserver) {
        RemoveObserver removeObserver = new RemoveObserver(this.id);
//        connectionStore.add(removeObserver);
        return removeObserver;
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        System.out.println("init server");
        Server server = ServerBuilder.forPort(99).addService(new ServerNetworkHandle()).build().start();
        System.out.println("running server");
        server.awaitTermination();
        System.out.println("ended server");
    }
}

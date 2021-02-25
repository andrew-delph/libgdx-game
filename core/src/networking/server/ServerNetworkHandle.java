package networking.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import infra.entity.Entity;
import infra.entity.EntityManager;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import modules.App;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;
import networking.server.connetion.ConnectionStore;
import networking.server.connetion.CreateConnection;
import networking.server.connetion.UpdateConnection;
import networking.server.observers.CreateObserver;
import networking.server.observers.RemoveObserver;
import networking.server.observers.UpdateObserver;

import java.io.IOException;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

    final public EntityManager entityManager;
    final public ConnectionStore connectionStore;
    final ServerObserverFactory serverObserverFactory;
    final private Server server;

    @Inject
    public ServerNetworkHandle(EntityManager entityManager, ConnectionStore connectionStore, ServerObserverFactory serverObserverFactory) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.serverObserverFactory = serverObserverFactory;
        server = ServerBuilder.forPort(99).addService(this).build();
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
        CreateObserver createObserver = this.serverObserverFactory.createCreateObserver();
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
        UpdateObserver updateObserver = this.serverObserverFactory.createUpdateObserver();
        connectionStore.add(new UpdateConnection(responseObserver));
        return updateObserver;
    }

    @Override
    public StreamObserver<NetworkObject.RemoveNetworkObject> remove(StreamObserver<NetworkObject.RemoveNetworkObject> responseObserver) {
        RemoveObserver removeObserver = this.serverObserverFactory.createRemoveObserver();
        return removeObserver;
    }
    public void awaitTermination() throws InterruptedException {
        this.server.awaitTermination();
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        System.out.println("init server");
        Injector injector = Guice.createInjector(new App());
        ServerNetworkHandle server = injector.getInstance(ServerNetworkHandle.class);
        server.awaitTermination();
    }
}

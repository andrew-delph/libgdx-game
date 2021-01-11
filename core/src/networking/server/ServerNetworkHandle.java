package networking.server;

import infra.entity.Entity;
import infra.entity.EntityManager;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;

import java.io.IOException;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

    EntityManager entityManager;

    public ServerNetworkHandle() {
        entityManager = EntityManager.getInstance();
    }

    @Override
    public StreamObserver<NetworkObject.CreateNetworkObject> create(StreamObserver<NetworkObject.CreateNetworkObject> responseObserver) {
        CreateObserver createObserver = new CreateObserver();
        System.out.println("new connection");
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId("what up").build();
        for (Entity entity : this.entityManager.getAll()) {
            createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(entity.getEntityData().getID().toString()).build();
            responseObserver.onNext(createRequestObject);
        }
        responseObserver.onNext(createRequestObject);
        return createObserver;
    }

    @Override
    public StreamObserver<NetworkObject.UpdateNetworkObject> update(StreamObserver<NetworkObject.UpdateNetworkObject> responseObserver) {
        UpdateObserver updateObserver = new UpdateObserver();
        return updateObserver;
    }

    @Override
    public StreamObserver<NetworkObject.RemoveNetworkObject> remove(StreamObserver<NetworkObject.RemoveNetworkObject> responseObserver) {
        RemoveObserver removeObserver = new RemoveObserver();
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

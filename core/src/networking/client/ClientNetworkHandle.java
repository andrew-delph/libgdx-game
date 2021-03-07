package networking.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import infra.entity.EntityManager;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.App;
import networking.NetworkObject;
import networking.NetworkObjectServiceGrpc;
import networking.client.observers.ClientObserverFactory;

import java.util.Scanner;

public class ClientNetworkHandle {

    public static String host = "localhost";
    public static int port = 99;
    final public EntityManager entityManager;

    private final ManagedChannel channel;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockingStub;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;


    public  StreamObserver<NetworkObject.CreateNetworkObject> createObserver;
    public  StreamObserver<NetworkObject.UpdateNetworkObject> updateObserver;
    public  StreamObserver<NetworkObject.RemoveNetworkObject> removeObserver;
    // responders
    public  StreamObserver<NetworkObject.CreateNetworkObject> createRequest;
    public  StreamObserver<NetworkObject.UpdateNetworkObject> updateRequest;
    public  StreamObserver<NetworkObject.RemoveNetworkObject> removeRequest;
    public ClientObserverFactory clientObserverFactory;

    @Inject
    public ClientNetworkHandle(EntityManager entityManager, ClientObserverFactory clientObserverFactory) {
        this.clientObserverFactory = clientObserverFactory;
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
        this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
        this.entityManager = entityManager;

        createObserver = this.clientObserverFactory.createCreateObserver();
        updateObserver = this.clientObserverFactory.createUpdateObserver();
        removeObserver = this.clientObserverFactory.createRemoveObserver();
    }

    public void connect(){
        createRequest = this.asyncStub.create(createObserver);
        updateRequest = this.asyncStub.update(updateObserver);
        removeRequest = this.asyncStub.remove(removeObserver);
    }

    public void disconnect(){
        this.createRequest.onCompleted();
        this.updateRequest.onCompleted();
        this.removeRequest.onCompleted();
        this.channel.shutdown();
    }

    public static void main(String args[]) throws InterruptedException {
        Injector injector = Guice.createInjector(
                new App()
        );

        Scanner myInput = new Scanner(System.in);

        ClientNetworkHandle client = injector.getInstance(ClientNetworkHandle.class);

        System.out.println("starting..!");
        while (true) {
            String id = myInput.nextLine();
            NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(id).build();
            client.createRequest.onNext(createRequestObject);
        }
    }

}

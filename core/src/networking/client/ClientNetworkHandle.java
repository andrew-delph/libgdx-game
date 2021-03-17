package networking.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import infra.events.Event;
import infra.events.EventService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.App;
import networking.NetworkObject;
import networking.NetworkObjectFactory;
import networking.NetworkObjectServiceGrpc;
import networking.client.observers.ClientObserverFactory;
import networking.connetion.ConnectionStore;
import networking.events.CreateEntityEvent;
import networking.events.RemoveEntityEvent;
import networking.events.UpdateEntityEvent;

import java.util.Scanner;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientNetworkHandle {

    public static String host = "localhost";
    public static int port = 99;
    final public EntityManager entityManager;

    private final ManagedChannel channel;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockingStub;
    private final NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;


    public StreamObserver<NetworkObject.CreateNetworkObject> createObserver;
    public StreamObserver<NetworkObject.UpdateNetworkObject> updateObserver;
    public StreamObserver<NetworkObject.RemoveNetworkObject> removeObserver;
    // responders
    public StreamObserver<NetworkObject.CreateNetworkObject> createRequest;
    public StreamObserver<NetworkObject.UpdateNetworkObject> updateRequest;
    public StreamObserver<NetworkObject.RemoveNetworkObject> removeRequest;
    public ClientObserverFactory clientObserverFactory;
    EventService eventService;
    EntityFactory entityFactory;

    @Inject
    public ClientNetworkHandle(EntityManager entityManager, ConnectionStore connectionStore, ClientObserverFactory clientObserverFactory, EventService eventService, EntityFactory entityFactory, NetworkObjectFactory networkObjectFactory) {
        this.clientObserverFactory = clientObserverFactory;
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
        this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
        this.entityManager = entityManager;
        this.eventService = eventService;
        this.entityFactory = entityFactory;


    }

    public void connect() {

        createObserver = this.clientObserverFactory.createCreateObserver();
        updateObserver = this.clientObserverFactory.createUpdateObserver();
        removeObserver = this.clientObserverFactory.createRemoveObserver();

        createRequest = this.asyncStub.create(createObserver);
        updateRequest = this.asyncStub.update(updateObserver);
        removeRequest = this.asyncStub.remove(removeObserver);

        this.eventService.addListener(CreateEntityEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                EntityData entityData = (EntityData) event.getData().get("entityData");
                Entity createEntity = entityFactory.create(entityData);
                entityManager.add(createEntity);
            }
        });
        this.eventService.addListener(UpdateEntityEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                EntityData entityData = (EntityData) event.getData().get("entityData");
                UUID targetUuid = UUID.fromString(entityData.getID());
                Entity target = entityManager.get(targetUuid);
                if (target == null) {
                    return;
                }
                target.updateEntityData(entityData);
            }
        });
        this.eventService.addListener(RemoveEntityEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                EntityData entityData = (EntityData) event.getData().get("entityData");
                UUID targetUuid = UUID.fromString(entityData.getID());
                Entity target = entityManager.get(targetUuid);
                if (target == null) {
                    return;
                }
                entityManager.remove(entityData.getID());
            }
        });
    }

    public void disconnect() {
        this.createRequest.onCompleted();
        this.updateRequest.onCompleted();
        this.removeRequest.onCompleted();
        this.channel.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
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

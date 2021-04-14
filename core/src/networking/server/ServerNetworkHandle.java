package networking.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import configure.CoreApp;
import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import infra.events.Event;
import infra.events.EventService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectFactory;
import networking.NetworkObjectServiceGrpc;
import networking.connetion.*;
import networking.events.CreateEntityEvent;
import networking.events.DisconnectEvent;
import networking.events.RemoveEntityEvent;
import networking.events.UpdateEntityEvent;
import networking.server.observers.CreateObserver;
import networking.server.observers.RemoveObserver;
import networking.server.observers.ServerObserverFactory;
import networking.server.observers.UpdateObserver;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

    final public EntityManager entityManager;
    final public ConnectionStore connectionStore;
    final ServerObserverFactory serverObserverFactory;
    final private Server server;
    EventService eventService;
    EntityFactory entityFactory;
    NetworkObjectFactory networkObjectFactory;


    @Inject
    public ServerNetworkHandle(EntityManager entityManager, ConnectionStore connectionStore, ServerObserverFactory serverObserverFactory, EventService eventService, EntityFactory entityFactory, NetworkObjectFactory networkObjectFactory) {
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.serverObserverFactory = serverObserverFactory;
        this.eventService = eventService;
        this.entityFactory = entityFactory;
        this.networkObjectFactory = networkObjectFactory;
        server = ServerBuilder.forPort(99).addService(this).build();


//        this.eventService.addListener(RemoveEntityEvent.type, new Consumer<Event>() {
//            @Override
//            public void accept(Event event) {
//
//            }
//        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new CoreApp());
        ServerNetworkHandle server = injector.getInstance(ServerNetworkHandle.class);
        server.start();
        server.awaitTermination();
    }

    public void start() throws IOException {
        this.eventService.addListener(CreateEntityEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                EntityData entityData = (EntityData) event.getData().get("entityData");
                StreamObserver<NetworkObject.CreateNetworkObject> requestObserver = (StreamObserver<NetworkObject.CreateNetworkObject>) event.getData().get("requestObserver");
                Entity createEntity = entityFactory.create(entityData);
                entityManager.add(createEntity);
                connectionStore.getAll(CreateConnection.class).forEach(createConnection -> {
                    if (createConnection.requestObserver == requestObserver) {
                    } else {
                        System.out.println("send");
                        createConnection.requestObserver.onNext(networkObjectFactory.createNetworkObject(entityData));
                    }
                });
            }
        });
        this.eventService.addListener(UpdateEntityEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                EntityData entityData = (EntityData) event.getData().get("entityData");
                StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver = (StreamObserver<NetworkObject.RemoveNetworkObject>) event.getData().get("requestObserver");
                UUID targetUuid = UUID.fromString(entityData.getID());
                Entity target = entityManager.get(targetUuid);
                if (target == null) {
                    return;
                }
                target.updateEntityData(entityData);
                connectionStore.getAll(UpdateConnection.class).forEach(updateConnection -> {
                    if (updateConnection.requestObserver == requestObserver) {
                    } else {
                        updateConnection.requestObserver.onNext(networkObjectFactory.updateNetworkObject(entityData));
                    }
                });
            }
        });
        this.eventService.addListener(RemoveEntityEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                EntityData entityData = (EntityData) event.getData().get("entityData");
                StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver = (StreamObserver<NetworkObject.RemoveNetworkObject>) event.getData().get("requestObserver");
                UUID targetUuid = UUID.fromString(entityData.getID());
                Entity target = entityManager.get(targetUuid);
                if (target == null) {
                    return;
                }
                entityManager.remove(entityData.getID());
                connectionStore.getAll(RemoveConnection.class).forEach(removeConnection -> {
                    if (removeConnection.requestObserver == requestObserver) {
                    } else {
                        removeConnection.requestObserver.onNext(networkObjectFactory.removeNetworkObject(entityData));
                    }
                });
            }
        });
        this.eventService.addListener(DisconnectEvent.type, new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                StreamObserver requestObserver = (StreamObserver) event.getData().get("requestObserver");
                AbtractConnection connection = connectionStore.get(requestObserver);
                connectionStore.remove(connection.id);
            }
        });
        server.start();
    }

    public void close() {
        server.shutdown();
        // TODO check if server closed
    }

    @Override
    public StreamObserver<NetworkObject.CreateNetworkObject> create(StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
        CreateObserver responseObserver = this.serverObserverFactory.createCreateObserver(requestObserver);
        CreateConnection connection = new CreateConnection(responseObserver, requestObserver);

        connectionStore.add(connection);
        for (Entity entity : this.entityManager.getAll()) {
            NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((entity.getEntityData().getX() + "")).build();
            NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((entity.getEntityData().getY() + "")).build();
            NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(entity.getEntityData().getID()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
            requestObserver.onNext(createRequestObject);
        }
        return responseObserver;
    }

    @Override
    public StreamObserver<NetworkObject.UpdateNetworkObject> update(StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
        UpdateObserver responseObserver = this.serverObserverFactory.createUpdateObserver(requestObserver);
        UpdateConnection connection = new UpdateConnection(responseObserver, requestObserver);
        connectionStore.add(connection);
        return responseObserver;
    }

    @Override
    public StreamObserver<NetworkObject.RemoveNetworkObject> remove(StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
        RemoveObserver responseObserver = this.serverObserverFactory.createRemoveObserver(requestObserver);
        connectionStore.add(new RemoveConnection(responseObserver, requestObserver));
        return responseObserver;
    }

    public void awaitTermination() throws InterruptedException {
        this.server.awaitTermination();
    }
}

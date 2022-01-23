package networking.server;

import app.user.User;
import app.user.UserID;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.GameStore;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;
import networking.*;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.GetChunkOutgoingEventType;
import networking.events.types.outgoing.HandshakeOutgoingEventType;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
    @Inject
    ObserverFactory observerFactory;
    @Inject
    ConnectionStore connectionStore;
    @Inject
    GameStore gameStore;
    @Inject
    ChunkFactory chunkFactory;
    @Inject
    EventTypeFactory eventTypeFactory;
    @Inject
    ChunkSubscriptionManager chunkSubscriptionManager;
    @Inject
    User user;
    private Server server;

    @Inject
    public ServerNetworkHandle() {
        System.out.println("I am server: " + this.uuid);
    }

    public void start() throws IOException {
        System.out.println("server listen");
        server =
                ServerBuilder.forPort(99)
                        .addService(this)
                        .addService(ProtoReflectionService.newInstance())
                        .build();
        server.start();
    }

    @Override
    public StreamObserver<NetworkObjects.NetworkEvent> networkObjectStream(
            StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {
        RequestNetworkEventObserver requestNetworkEventObserver = observerFactory.create();
        requestNetworkEventObserver.responseObserver = responseObserver;
        NetworkObjects.NetworkEvent authenticationEvent =
                NetworkObjects.NetworkEvent.newBuilder()
                        .setEvent("authentication")
                        .setUser(user.getUserID().toString())
                        .build();
        requestNetworkEventObserver.responseObserver.onNext(authenticationEvent);
        return requestNetworkEventObserver;
    }

    @Override
    public void getChunk(
            NetworkObjects.NetworkEvent request,
            StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {
        GetChunkOutgoingEventType realEvent = eventTypeFactory.createGetChunkOutgoingEventType(request);
        Chunk chunk = gameStore.getChunk(realEvent.getChunkRange());
        if (chunk == null) {
            chunk = this.chunkFactory.create(realEvent.getChunkRange());
        }
        chunkSubscriptionManager.registerSubscription(realEvent.getUserID(), realEvent.getChunkRange());
        responseObserver.onNext(
                NetworkObjects.NetworkEvent.newBuilder()
                        .setData(chunk.toNetworkData())
                        .setEvent("get_chunk")
                        .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getEntity(
            NetworkObjects.NetworkEvent request,
            StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    public void close() {
        this.server.shutdown();
    }

    public synchronized void send(UserID userID, NetworkObjects.NetworkEvent networkEvent) {
        networkEvent = networkEvent.toBuilder().setUser(user.getUserID().toString()).build();
        connectionStore.getConnection(userID).responseObserver.onNext(networkEvent);
    }

    public void initHandshake(UserID userID, ChunkRange chunkRange) {
        List<UUID> uuidList = new LinkedList<>(this.gameStore.getChunk(chunkRange).getEntityUUIDSet());
        HandshakeOutgoingEventType handshakeOutgoing = EventTypeFactory.
                createHandshakeOutgoingEventType(chunkRange, uuidList);
        this.send(userID, handshakeOutgoing.toNetworkEvent());

        System.out.println("SERVER INIT HANDSHAKE " + userID.toString());
    }
}

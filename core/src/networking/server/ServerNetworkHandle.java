package networking.server;

import app.user.User;
import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import common.GameStore;
import generation.ChunkGenerationService;
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
    ActiveChunkManager activeChunkManager;
    @Inject
    User user;
    @Inject
    ChunkGenerationService chunkGenerationService;
    private Server server;

    @Inject
    public ServerNetworkHandle() {
    }

    public void start() throws IOException {
        System.out.println("I am server: " + this.user.toString());
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

        try {
            chunkGenerationService.blockedChunkRangeToGenerate(realEvent.getChunkRange());
        } catch (Exception e) {
            responseObserver.onError(e);
            return;
        }

        activeChunkManager.addUserChunkSubscriptions(realEvent.getUserID(), realEvent.getChunkRange());

        Chunk chunk = gameStore.getChunk(realEvent.getChunkRange());

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
        responseObserver.onNext(request.toBuilder().setUser(user.getUserID().toString()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void connections(Empty request, StreamObserver<NetworkObjects.ConnectionsData> responseObserver) {
        NetworkObjects.ConnectionsData connectionsData = NetworkObjects.ConnectionsData.newBuilder().setCount(69).build();
        responseObserver.onNext(connectionsData);
        responseObserver.onCompleted();
    }

    public void close() {
        this.server.shutdown();
    }

    public synchronized void send(UserID userID, NetworkObjects.NetworkEvent networkEvent) {
        networkEvent = networkEvent.toBuilder().setUser(user.getUserID().toString()).build();
        RequestNetworkEventObserver observer = connectionStore.getConnection(userID);
        observer.responseObserver.onNext(networkEvent);
    }

    public void initHandshake(UserID userID, ChunkRange chunkRange) {
        List<UUID> uuidList = new LinkedList<>(this.gameStore.getChunk(chunkRange).getEntityUUIDSet());
        HandshakeOutgoingEventType handshakeOutgoing = EventTypeFactory.
                createHandshakeOutgoingEventType(chunkRange, uuidList);
        this.send(userID, handshakeOutgoing.toNetworkEvent());
        System.out.println("SERVER INIT HANDSHAKE " + userID.toString());
    }
}

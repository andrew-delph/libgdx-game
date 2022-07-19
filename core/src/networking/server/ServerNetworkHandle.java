package networking.server;

import app.user.User;
import app.user.UserID;
import chunk.ActiveChunkManager;
import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import common.Clock;
import common.GameSettings;
import common.GameStore;
import generation.ChunkGenerationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import networking.ConnectionStore;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;
import networking.NetworkObjects.Version;
import networking.ObserverFactory;
import networking.RequestNetworkEventObserver;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.GetChunkOutgoingEventType;
import networking.events.types.outgoing.HandshakeOutgoingEventType;
import networking.ping.PingService;
import networking.sync.SyncService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
  final Logger LOGGER = LogManager.getLogger();
  @Inject ObserverFactory observerFactory;
  @Inject ConnectionStore connectionStore;
  @Inject GameStore gameStore;
  @Inject EventTypeFactory eventTypeFactory;
  @Inject ActiveChunkManager activeChunkManager;
  @Inject User user;
  @Inject ChunkGenerationService chunkGenerationService;
  @Inject GameSettings gameSettings;
  @Inject PingService pingService;
  @Inject SyncService syncService;
  @Inject Clock clock;
  private Server server;

  @Inject
  public ServerNetworkHandle() {}

  public void start() throws IOException {
    LOGGER.info("I am server: " + this.user.toString());
    server =
        ServerBuilder.forPort(99)
            .addService(this)
            .addService(ProtoReflectionService.newInstance())
            .build();
    server.start();
    pingService.start();
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
  public void health(Empty request, StreamObserver<NetworkObjects.Health> responseObserver) {

    if (clock.getCurrentTick().time > 64000 / 16 * 2 * 60) { // 2 hours
      responseObserver.onError(Status.NOT_FOUND.withDescription("unhealth").asException());
      return;
    }

    NetworkObjects.Health healthData =
        NetworkObjects.Health.newBuilder()
            .setHealthy(true)
            .setId(this.user.getUserID().toString())
            .setConnections(connectionStore.size())
            .build();
    responseObserver.onNext(healthData);
    responseObserver.onCompleted();
  }

  @Override
  public void getVersion(Empty request, StreamObserver<Version> responseObserver) {
    NetworkObjects.Version versionData =
        NetworkObjects.Version.newBuilder().setVersion(gameSettings.getVersion()).build();
    responseObserver.onNext(versionData);
    responseObserver.onCompleted();
  }

  public void close() {
    this.server.shutdown();
  }

  public synchronized void send(UserID userID, NetworkObjects.NetworkEvent networkEvent) {
    networkEvent =
        networkEvent.toBuilder()
            .setUser(user.getUserID().toString())
            .setTime(Clock.getCurrentTime())
            .build();
    RequestNetworkEventObserver observer = connectionStore.getConnection(userID);
    observer.responseObserver.onNext(networkEvent);
  }

  public synchronized void initHandshake(UserID userID, ChunkRange chunkRange) {
    if (syncService.isHandshakeLocked(userID, chunkRange)) {
      LOGGER.info("SERVER INIT LOCKED " + userID.toString() + " " + chunkRange);
      return;
    }
    syncService.lockHandshake(userID, chunkRange, GameSettings.HANDSHAKE_TIMEOUT);
    List<UUID> uuidList = new LinkedList<>(this.gameStore.getChunk(chunkRange).getEntityUUIDSet());
    HandshakeOutgoingEventType handshakeOutgoing =
        EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, uuidList);
    this.send(userID, handshakeOutgoing.toNetworkEvent());
    LOGGER.info("SERVER INIT HANDSHAKE " + userID.toString() + " " + chunkRange);
  }
}

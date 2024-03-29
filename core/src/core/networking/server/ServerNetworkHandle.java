package core.networking.server;

import static core.common.Util.calcTicksFromHours;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import core.app.game.Game;
import core.app.game.GameController;
import core.app.user.User;
import core.app.user.UserID;
import core.chunk.ActiveChunkManager;
import core.chunk.Chunk;
import core.common.ChunkRange;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.entity.ActiveEntityManager;
import core.entity.Entity;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.groups.Group;
import core.entity.groups.GroupService;
import core.generation.ChunkGenerationService;
import core.networking.ConnectionStore;
import core.networking.ObserverFactory;
import core.networking.RequestNetworkEventObserver;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.outgoing.GetChunkOutgoingEventType;
import core.networking.events.types.outgoing.HandshakeOutgoingEventType;
import core.networking.ping.PingService;
import core.networking.sync.SyncService;
import core.networking.translation.NetworkDataDeserializer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;
import networking.NetworkObjects.STATUS;
import networking.NetworkObjects.Version;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

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
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject GroupService groupService;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject Game game;
  private Server server;

  @Inject
  public ServerNetworkHandle() {}

  public void start() throws IOException {
    Gdx.app.log(GameSettings.LOG_TAG, "I am server: " + this.user.toString());
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

    UserID requestedUser = UserID.createUserID(request.getUser());

    Coordinates coordinates = NetworkDataDeserializer.createCoordinates(request.getData());

    Entity returnEntity = null;
    try {
      returnEntity =
          gameController.createEntity(
              coordinates,
              (entity -> {
                entity.setEntityController(
                    entityControllerFactory.createRemoteBodyController(entity));

                groupService.registerEntityGroup(entity.getUuid(), Group.PLAYER_GROUP);
                activeEntityManager.registerActiveEntity(requestedUser, entity.getUuid());
              }));
    } catch (ChunkNotFound e) {
      e.printStackTrace();
      return;
    }

    responseObserver.onNext(request.toBuilder().setData(returnEntity.toNetworkData()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void health(Empty request, StreamObserver<NetworkObjects.Health> responseObserver) {

    if (clock.getCurrentTick().time > calcTicksFromHours(24)) {
      responseObserver.onError(Status.NOT_FOUND.withDescription("unhealth").asException());
      return;
    }

    NetworkObjects.STATUS serverStatus =
        (connectionStore.size() > 0) ? STATUS.ACTIVE : STATUS.INACTIVE;

    Instant now = Instant.now();

    Duration upTime = Duration.between(game.gameStartTime, now);

    NetworkObjects.Health.Builder healthDataBuilder =
        NetworkObjects.Health.newBuilder()
            .setId(this.user.getUserID().toString())
            .setStatus(serverStatus)
            .setUptime(upTime.toMillis())
            .setConnections(connectionStore.size());

    if (connectionStore.inactiveStartTime != null) {
      Duration inActiveTime = Duration.between(connectionStore.inactiveStartTime, now);
      healthDataBuilder.setInactiveTime(inActiveTime.toMillis());
    }

    responseObserver.onNext(healthDataBuilder.build());
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
      Gdx.app.log(
          GameSettings.LOG_TAG, "SERVER INIT LOCKED " + userID.toString() + " " + chunkRange);
      return;
    }
    syncService.lockHandshake(userID, chunkRange, GameSettings.HANDSHAKE_TIMEOUT);
    List<UUID> uuidList = new LinkedList<>(this.gameStore.getChunk(chunkRange).getEntityUUIDSet());
    HandshakeOutgoingEventType handshakeOutgoing =
        EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, uuidList);
    this.send(userID, handshakeOutgoing.toNetworkEvent());
    Gdx.app.log(
        GameSettings.LOG_TAG, "SERVER INIT HANDSHAKE " + userID.toString() + " " + chunkRange);
  }
}

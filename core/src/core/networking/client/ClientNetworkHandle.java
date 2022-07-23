package core.networking.client;

import com.google.inject.Inject;
import com.google.protobuf.Empty;
import com.sun.tools.javac.util.Pair;
import core.app.user.User;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.common.ChunkRange;
import core.common.Clock;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.entity.Entity;
import core.networking.ObserverFactory;
import core.networking.RequestNetworkEventObserver;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.outgoing.GetChunkOutgoingEventType;
import core.networking.events.types.outgoing.HandshakeOutgoingEventType;
import core.networking.ping.PingService;
import core.networking.sync.SyncService;
import core.networking.translation.DataTranslationEnum;
import core.networking.translation.NetworkDataDeserializer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientNetworkHandle {
  public final CountDownLatch authLatch = new CountDownLatch(1);
  final Logger LOGGER = LogManager.getLogger();
  public String host = "localhost";
  public int port = 99;
  RequestNetworkEventObserver requestNetworkEventObserver;
  @Inject ObserverFactory observerFactory;
  @Inject EventTypeFactory eventTypeFactory;
  @Inject NetworkDataDeserializer entitySerializationConverter;
  @Inject GameStore gameStore;
  @Inject ChunkFactory chunkFactory;
  @Inject User user;
  @Inject GameSettings gameSettings;
  @Inject PingService pingService;
  @Inject SyncService syncService;

  private ManagedChannel channel;
  private NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;
  private NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockStub;

  @Inject
  public ClientNetworkHandle() {}

  public void connect() throws InterruptedException, WrongVersion {
    LOGGER.info(
        "I am client: "
            + this.user.toString()
            + ". Connecting to "
            + gameSettings.getHost()
            + ":"
            + gameSettings.getPort());
    if (gameSettings.getHost().equals("localhost")) {
      this.channel =
          ManagedChannelBuilder.forAddress(gameSettings.getHost(), gameSettings.getPort())
              .usePlaintext()
              .build();
    } else {
      this.channel =
          ManagedChannelBuilder.forAddress(gameSettings.getHost(), gameSettings.getPort()).build();
    }
    this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
    this.blockStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
    requestNetworkEventObserver = observerFactory.create();
    requestNetworkEventObserver.responseObserver =
        this.asyncStub.networkObjectStream(requestNetworkEventObserver);

    this.checkVersion();

    NetworkObjects.NetworkEvent authenticationEvent =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.AUTH).build();
    this.send(authenticationEvent);
    if (!authLatch.await(5, TimeUnit.SECONDS)) {
      throw new InterruptedException("did not receive auth information");
    }
    pingService.start();
  }

  public synchronized void send(NetworkObjects.NetworkEvent networkEvent) {
    networkEvent =
        networkEvent.toBuilder()
            .setUser(this.user.toString())
            .setTime(Clock.getCurrentTime())
            .build();
    requestNetworkEventObserver.responseObserver.onNext(networkEvent);
  }

  public Chunk requestChunkBlocking(ChunkRange chunkRange) throws SerializationDataMissing {
    Chunk myChunk;
    synchronized (this) {
      if (gameStore.doesChunkExist(chunkRange)) {
        return gameStore.getChunk(chunkRange);
      } else {
        // make the chunk
        myChunk = chunkFactory.create(chunkRange);
        gameStore.addChunk(myChunk);
      }
    }

    GetChunkOutgoingEventType outgoing =
        eventTypeFactory.createGetChunkOutgoingEventType(chunkRange, this.user.getUserID());
    NetworkObjects.NetworkEvent retrievedNetworkEvent =
        this.blockStub.getChunk(outgoing.toNetworkEvent());

    Pair<ChunkRange, List<Entity>> chunkData =
        entitySerializationConverter.createChunkData(retrievedNetworkEvent.getData());

    for (Entity toAdd : chunkData.snd) {
      try {
        gameStore.addEntity(toAdd);
      } catch (ChunkNotFound e) {
        LOGGER.error(e);
      }
    }

    return myChunk;
  }

  public boolean requestChunkAsync(ChunkRange requestedChunkRange) {
    Chunk myChunk;
    // if chunk range is locked. return false
    // if not. lock it
    synchronized (this) {
      if (gameStore.doesChunkExist(requestedChunkRange)) {
        return false;
      } else {
        // make the chunk
        myChunk = chunkFactory.create(requestedChunkRange);
        gameStore.addChunk(myChunk);
      }
    }

    GetChunkOutgoingEventType outgoing =
        eventTypeFactory.createGetChunkOutgoingEventType(
            requestedChunkRange, this.user.getUserID());
    // make the async request
    // at the end of the request remove the lock
    asyncStub.getChunk(
        outgoing.toNetworkEvent(),
        new StreamObserver<NetworkObjects.NetworkEvent>() {

          @Override
          public void onNext(NetworkObjects.NetworkEvent networkEvent) {
            // calls data received
            try {
              Pair<ChunkRange, List<Entity>> chunkData =
                  entitySerializationConverter.createChunkData(networkEvent.getData());
              for (Entity toAdd : chunkData.snd) {
                try {
                  gameStore.addEntity(toAdd);
                } catch (ChunkNotFound e) {
                  LOGGER.error(e);
                  onCompleted();
                }
              }
            } catch (SerializationDataMissing e) {
              e.printStackTrace();
            }
          }

          @Override
          public void onError(Throwable t) {
            gameStore.removeChunk(requestedChunkRange);
            t.printStackTrace();
          }

          @Override
          public void onCompleted() {}
        });
    // in the observer. at the end
    return true;
  }

  public boolean checkVersion() throws WrongVersion {
    NetworkObjects.Version versionData = this.blockStub.getVersion(Empty.newBuilder().build());
    if (!gameSettings.getVersion().equals(versionData.getVersion())) {
      throw new WrongVersion(
          String.format(
              "Client version: %s, Server version: %s",
              gameSettings.getVersion(), versionData.getVersion()));
    }
    return true;
  }

  public synchronized void initHandshake(ChunkRange chunkRange) {
    if (syncService.isHandshakeLocked(this.user.getUserID(), chunkRange)) {
      LOGGER.info("CLIENT INIT LOCKED " + " " + chunkRange);
      return;
    }
    syncService.lockHandshake(user.getUserID(), chunkRange, GameSettings.HANDSHAKE_TIMEOUT);
    HandshakeOutgoingEventType handshakeOutgoing =
        EventTypeFactory.createHandshakeOutgoingEventType(chunkRange);
    this.send(handshakeOutgoing.toNetworkEvent());
    LOGGER.info("CLIENT INIT HANDSHAKE " + chunkRange);
  }

  public void close() {
    this.requestNetworkEventObserver.responseObserver.onCompleted();
    this.channel.shutdown();
  }
}

package networking.client;

import app.user.User;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import com.sun.tools.javac.util.Pair;
import common.GameStore;
import common.exceptions.SerializationDataMissing;
import configuration.GameSettings;
import entity.Entity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;
import networking.ObserverFactory;
import networking.RequestNetworkEventObserver;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.GetChunkOutgoingEventType;
import networking.events.types.outgoing.HandshakeOutgoingEventType;
import networking.translation.NetworkDataDeserializer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static networking.translation.DataTranslationEnum.AUTH;

public class ClientNetworkHandle {
  public final CountDownLatch authLatch = new CountDownLatch(1);
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

  private ManagedChannel channel;
  private NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;
  private NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockStub;

  @Inject
  public ClientNetworkHandle() {}

  public void connect() throws InterruptedException {
    System.out.println(
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

    NetworkObjects.NetworkEvent authenticationEvent =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(AUTH).build();
    this.send(authenticationEvent);
    if (!authLatch.await(5, TimeUnit.SECONDS)) {
      throw new InterruptedException("did not receive auth information");
    }
  }

  public synchronized void send(NetworkObjects.NetworkEvent networkEvent) {
    networkEvent = networkEvent.toBuilder().setUser(this.user.toString()).build();
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
    myChunk.addAllEntity(chunkData.snd);

    gameStore.addChunk(myChunk);

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
              myChunk.addAllEntity(chunkData.snd);
              gameStore.addChunk(myChunk);
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

  public void initHandshake(ChunkRange chunkRange) {
    HandshakeOutgoingEventType handshakeOutgoing =
        EventTypeFactory.createHandshakeOutgoingEventType(chunkRange);
    this.send(handshakeOutgoing.toNetworkEvent());
    System.out.println("CLIENT INIT HANDSHAKE " + this.user.toString());
  }

  public void close() {
    this.requestNetworkEventObserver.responseObserver.onCompleted();
    this.channel.shutdown();
  }
}

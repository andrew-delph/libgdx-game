package networking.client;

import app.user.User;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.sun.tools.javac.util.Pair;
import common.GameStore;
import common.exceptions.SerializationDataMissing;
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
import java.util.Set;
import java.util.UUID;

public class ClientNetworkHandle {
    private final Set<ChunkRange> chunkRangeLock = Sets.newConcurrentHashSet();
    public String host = "localhost";
    public int port = 99;
    RequestNetworkEventObserver requestNetworkEventObserver;
    @Inject
    ObserverFactory observerFactory;
    @Inject
    EventTypeFactory eventTypeFactory;
    @Inject
    NetworkDataDeserializer entitySerializationConverter;
    @Inject
    GameStore gameStore;
    @Inject
    ChunkFactory chunkFactory;
    @Inject
    User user;

    private ManagedChannel channel;
    private NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;
    private NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockStub;

    @Inject
    public ClientNetworkHandle() {
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        System.out.println("I am client: " + this.user.toString());
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
        this.blockStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
        requestNetworkEventObserver = observerFactory.create();
        requestNetworkEventObserver.responseObserver =
                this.asyncStub.networkObjectStream(requestNetworkEventObserver);

        NetworkObjects.NetworkEvent authenticationEvent =
                NetworkObjects.NetworkEvent.newBuilder().setEvent("authentication").build();

        this.send(authenticationEvent);
    }

    public synchronized void send(NetworkObjects.NetworkEvent networkEvent) {
        networkEvent = networkEvent.toBuilder().setUser(this.user.toString()).build();
        requestNetworkEventObserver.responseObserver.onNext(networkEvent);
    }

    public Chunk requestChunkBlocking(ChunkRange chunkRange) throws SerializationDataMissing {
        GetChunkOutgoingEventType outgoing = eventTypeFactory.createGetChunkOutgoingEventType(chunkRange, this.user.getUserID());
        NetworkObjects.NetworkEvent retrievedNetworkEvent = this.blockStub.getChunk(outgoing.toNetworkEvent());
        return entitySerializationConverter.createChunk(retrievedNetworkEvent.getData());
    }

    public boolean requestChunkAsync(ChunkRange requestedChunkRange) {
        // if chunk range is locked. return false
        // if not. lock it
        synchronized (this) {
            if (chunkRangeLock.contains(requestedChunkRange)) {
                return false;
            } else {
                chunkRangeLock.add(requestedChunkRange);
            }
        }
        // make the chunk
        Chunk myChunk = chunkFactory.create(requestedChunkRange);
        gameStore.addChunk(myChunk);

        GetChunkOutgoingEventType outgoing = eventTypeFactory.createGetChunkOutgoingEventType(requestedChunkRange, this.user.getUserID());
        // make the async request
        // at the end of the request remove the lock
        asyncStub.getChunk(outgoing.toNetworkEvent(), new StreamObserver<NetworkObjects.NetworkEvent>() {

            @Override
            public void onNext(NetworkObjects.NetworkEvent networkEvent) {
                // calls data received
                try {
                    Pair<ChunkRange, List<Entity>> chunkData = entitySerializationConverter.createChunkData(networkEvent.getData());
                    myChunk.addAllEntity(chunkData.snd);
                } catch (SerializationDataMissing e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                chunkRangeLock.remove(requestedChunkRange);
                gameStore.removeChunk(requestedChunkRange);
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // todo when is this called
                chunkRangeLock.remove(requestedChunkRange);
            }
        });
        // in the observer. at the end
        return true;
    }

    public void initHandshake(ChunkRange chunkRange) {
        HandshakeOutgoingEventType handshakeOutgoing = EventTypeFactory.
                createHandshakeOutgoingEventType(chunkRange);
        this.send(handshakeOutgoing.toNetworkEvent());
        System.out.println("CLIENT INIT HANDSHAKE " + this.user.toString());
    }

    public void close() {
        this.requestNetworkEventObserver.responseObserver.onCompleted();
        this.channel.shutdown();
    }
}

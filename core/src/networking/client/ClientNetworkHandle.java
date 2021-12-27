package networking.client;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.exceptions.SerializationDataMissing;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;
import networking.ObserverFactory;
import networking.RequestNetworkEventObserver;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.GetChunkOutgoingEventType;
import networking.events.types.outgoing.HandshakeOutgoingEventType;
import networking.translation.NetworkDataDeserializer;

import java.util.UUID;

public class ClientNetworkHandle {
    public String host = "localhost";
    public int port = 99;
    public final UUID uuid = UUID.randomUUID();
    RequestNetworkEventObserver requestNetworkEventObserver;
    @Inject
    ObserverFactory observerFactory;
    @Inject
    EventTypeFactory eventTypeFactory;
    @Inject
    NetworkDataDeserializer entitySerializationConverter;
    private ManagedChannel channel;
    private NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub;
    private NetworkObjectServiceGrpc.NetworkObjectServiceBlockingStub blockStub;

    @Inject
    public ClientNetworkHandle() {
        System.out.println("client: " + this.uuid);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = NetworkObjectServiceGrpc.newStub(channel);
        this.blockStub = NetworkObjectServiceGrpc.newBlockingStub(channel);
        requestNetworkEventObserver = observerFactory.create();
        requestNetworkEventObserver.responseObserver =
                this.asyncStub.networkObjectStream(requestNetworkEventObserver);

        asyncStub.getChunk(null, null);

        NetworkObjects.NetworkEvent authenticationEvent =
                NetworkObjects.NetworkEvent.newBuilder().setEvent("authentication").build();

        this.send(authenticationEvent);
    }

    public synchronized void send(NetworkObjects.NetworkEvent networkEvent) {
        networkEvent = networkEvent.toBuilder().setUser(this.uuid.toString()).build();
        requestNetworkEventObserver.responseObserver.onNext(networkEvent);
    }

    public Chunk getChunk(ChunkRange chunkRange) throws SerializationDataMissing {

        GetChunkOutgoingEventType realEvent = eventTypeFactory.createGetChunkOutgoingEventType(chunkRange, this.uuid);

        NetworkObjects.NetworkEvent retrievedNetworkEvent = this.blockStub.getChunk(realEvent.toNetworkEvent());

        return entitySerializationConverter.createChunk(retrievedNetworkEvent.getData());
    }

    public void initHandshake(ChunkRange chunkRange) {
        HandshakeOutgoingEventType handshakeOutgoing = EventTypeFactory.
                createHandshakeOutgoingEventType(chunkRange);
        this.send(handshakeOutgoing.toNetworkEvent());
    }

    public void close() {
        this.requestNetworkEventObserver.responseObserver.onCompleted();
        this.channel.shutdown();
    }
}

package networking.server;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameStore;
import entity.EntitySerializationConverter;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;
import networking.*;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.GetChunkOutgoingEventType;

import java.io.IOException;
import java.util.UUID;

public class ServerNetworkHandle extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {
  public UUID uuid;
  @Inject ObserverFactory observerFactory;
  @Inject ConnectionStore connectionStore;
  @Inject GameStore gameStore;
  @Inject ChunkFactory chunkFactory;
  @Inject EventTypeFactory eventTypeFactory;
  private Server server;

  @Inject
  public ServerNetworkHandle() {
    this.uuid = UUID.randomUUID();
    System.out.println("server: " + this.uuid.toString());
  }

  public void start() throws IOException {
    System.out.println("server start");
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
            .setUser(this.uuid.toString())
            .build();
    requestNetworkEventObserver.responseObserver.onNext(authenticationEvent);
    return requestNetworkEventObserver;
  }

  @Override
  public void getChunk(
      NetworkObjects.NetworkEvent request,
      StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {

    GetChunkOutgoingEventType realEvent = eventTypeFactory.createGetChunkOutgoingEventType(request);

    //    Chunk chunk = gameStore.getChunk(realEvent.getChunkRange());
    Chunk chunk = gameStore.getChunk(realEvent.getChunkRange());
    if (chunk == null) {
      chunk = this.chunkFactory.create(realEvent.getChunkRange());
    }

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
    System.out.println(2222);
    responseObserver.onNext(request);
    responseObserver.onCompleted();
  }

  public void close() {
    this.server.shutdown();
  }

  public synchronized void send(UUID uuid, NetworkObjects.NetworkEvent networkEvent) {
    networkEvent = networkEvent.toBuilder().setUser(this.uuid.toString()).build();
    connectionStore.getConnection(uuid).responseObserver.onNext(networkEvent);
  }
}

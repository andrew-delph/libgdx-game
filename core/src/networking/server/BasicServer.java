package networking.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import networking.NetworkObjectServiceGrpc;
import networking.NetworkObjects;

public class BasicServer extends NetworkObjectServiceGrpc.NetworkObjectServiceImplBase {

  public static void main(String[] args) throws IOException, InterruptedException {
    BasicServer basicServer = new BasicServer();
    Server server = ServerBuilder.forPort(99).addService(basicServer).build();
    server.start();
    System.out.println("started");
    server.awaitTermination();
  }

  @Override
  public StreamObserver<NetworkObjects.NetworkEvent> networkObjectStream(
      StreamObserver<NetworkObjects.NetworkEvent> responseObserver) {
    System.out.println("HEOEEH");
    return new StreamObserver<NetworkObjects.NetworkEvent>() {

      @Override
      public void onNext(NetworkObjects.NetworkEvent value) {
        System.out.println("event: " + value.getEvent());
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("onError");
      }

      @Override
      public void onCompleted() {
        System.out.println("onCompleted");
      }
    };
  }
}

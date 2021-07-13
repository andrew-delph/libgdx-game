package infra.networking.client;

import java.util.concurrent.TimeUnit;

import infra.networking.NetworkObjectServiceGrpc;
import infra.networking.NetworkObjects;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class BasicClient {
    public static void main(String[] args) throws InterruptedException {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 99).usePlaintext().build();
        NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub = NetworkObjectServiceGrpc.newStub(channel);

        StreamObserver<NetworkObjects.NetworkEvent> sender = asyncStub.networkObjectStream(new StreamObserver<NetworkObjects.NetworkEvent>(){

            @Override
            public void onNext(NetworkObjects.NetworkEvent value) {
                System.out.println(value.getEvent());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("error");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("complete");
            }
        });

        NetworkObjects.NetworkEvent authenticationEvent =
                NetworkObjects.NetworkEvent.newBuilder().setEvent("andoid").build();

        sender.onNext(authenticationEvent);

        TimeUnit.SECONDS.sleep(3);

    System.out.println("here");
    }
}

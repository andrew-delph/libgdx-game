package org.shareable;



import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ShareableMapClient{

    private final ManagedChannel channel;
    private final ShareableMapServiceGrpc.ShareableMapServiceStub asyncStub;

    ConcurrentHashMap<String, String> shareableMap;

    public ShareableMapClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = ShareableMapServiceGrpc.newStub(channel);
        this.shareableMap = new ConcurrentHashMap();
    }

    public static void main(String args[]) throws InterruptedException {
        ShareableMapClient client = new ShareableMapClient("localhost", 99);
        client.chat();
    }

    public void chat() {
        Scanner myInput = new Scanner(System.in);

        // Control for received
        StreamObserver<ShareableProto.StreamableMapUpdate> chatResponse = new StreamObserver<ShareableProto.StreamableMapUpdate>() {
            @Override
            public void onNext(ShareableProto.StreamableMapUpdate update) {
                System.out.println("<<< " + update.getKey() + ":" + update.getValue());
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("error " + throwable);
            }
            @Override
            public void onCompleted() {
                System.out.println("COMPLETE");
            }
        };

        StreamObserver<ShareableProto.StreamableMapUpdate> chatRequest = this.asyncStub.streamShareableMap(chatResponse);

        System.out.println("Start chatting!");
        while (true) {
            System.out.println("enter key");
            String key = myInput.nextLine();
            System.out.println("enter value");
            String value = myInput.nextLine();
            ShareableProto.StreamableMapUpdate clientMessage = ShareableProto.StreamableMapUpdate.newBuilder().setKey(key).setValue(value).build();
            chatRequest.onNext(clientMessage);
        }
    }

}

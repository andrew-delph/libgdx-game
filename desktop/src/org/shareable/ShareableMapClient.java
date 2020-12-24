package org.shareable;



import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ShareableMapClient implements ShareableMap {

    private final ManagedChannel channel;
    private final ShareableMapServiceGrpc.ShareableMapServiceBlockingStub blockingStub;
    private final ShareableMapServiceGrpc.ShareableMapServiceStub asyncStub;

    ConcurrentHashMap<String, String> shareableMap;
    HashMap<Object, ServerStreamObserver> connectionMap;

    public ShareableMapClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ShareableMapServiceGrpc.newBlockingStub(channel);
        this.asyncStub = ShareableMapServiceGrpc.newStub(channel);
        this.shareableMap = new ConcurrentHashMap();
    }

    public static void main(String args[]) throws InterruptedException {
        ShareableMapClient client = new ShareableMapClient("localhost", 99);
        client.chat();
    }

    public void chat() {

        Scanner myInput = new Scanner(System.in);
        final ShareableMapClient grpcClient = this;

        // Control for received
        StreamObserver<ShareableProto.StreamableMapUpdate> chatResponse = new StreamObserver<ShareableProto.StreamableMapUpdate>() {
            @Override
            public void onNext(ShareableProto.StreamableMapUpdate update) {
                System.out.println("<<< " + update.getKey() + ":" + update.getValue());
                grpcClient.setValue(update.getKey(), update.getValue());
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

        StreamObserver<ShareableProto.StreamableMapUpdate> chatRequest = grpcClient.asyncStub.streamShareableMap(chatResponse);

        StreamObserver<ShareableProto.StreamableMapObject> mapResponse = new StreamObserver<ShareableProto.StreamableMapObject>() {
            @Override
            public void onNext(ShareableProto.StreamableMapObject update) {
                List<ShareableProto.StreamableMapUpdate> list = update.getStreamableMapList();
                for(ShareableProto.StreamableMapUpdate item: list){
                    System.out.println("map: "+item.getKey()+":"+item.getValue());
                }
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


        System.out.println("Start chatting!");
        while (true) {
            System.out.println("enter key");
            String key = myInput.nextLine();
            System.out.println("enter value");
            String value = myInput.nextLine();
            ShareableProto.StreamableMapUpdate clientMessage = ShareableProto.StreamableMapUpdate.newBuilder().setKey(key).setValue(value).build();
            chatRequest.onNext(clientMessage);

            grpcClient.asyncStub.getShareableMap(ShareableProto.RequestKeys.newBuilder().build(), mapResponse);
        }
    }

    @Override
    public void setValue(String key, String value) {
        this.shareableMap.put(key, value);
    }

    @Override
    public String getValue(String key) {
        return this.shareableMap.get(key);
    }

}

package org.shareable;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ShareableMapServer extends ShareableMapServiceGrpc.ShareableMapServiceImplBase{

    public static int count = 0;
    ConcurrentHashMap<String, String> shareableMap;
    HashMap<Object, StreamObserver> connectionMap;

    public ShareableMapServer() {
        this.shareableMap = new ConcurrentHashMap<String, String>();
        this.connectionMap = new HashMap<>();
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        System.out.println("init server");
        Server server = ServerBuilder.forPort(99).addService(new ShareableMapServer()).build().start();
        System.out.println("running server");
        server.awaitTermination();
        System.out.println("ended server");
    }

    public void relayShareableMapServer(Object key, ShareableProto.StreamableMapUpdate streamableMapUpdate) {
        System.out.println("sttarting"+this.connectionMap.size());
        streamableMapUpdate = ShareableProto.StreamableMapUpdate.newBuilder().setKey(streamableMapUpdate.getKey()).setValue(streamableMapUpdate.getValue()+"1").build();
        Iterator<Map.Entry<Object, StreamObserver>> map = this.connectionMap.entrySet().iterator();

        for (Iterator<Map.Entry<Object, StreamObserver>> it = map; it.hasNext(); ) {
            Map.Entry<Object, StreamObserver> me = it.next();

            System.out.println("sending.."+me.getKey());
            (me.getValue()).onNext(streamableMapUpdate);
        }
        System.out.println("sent.");
    }

    @Override
    public StreamObserver<ShareableProto.StreamableMapUpdate> streamShareableMap(StreamObserver<ShareableProto.StreamableMapUpdate> responseObserver) {
        int key = ShareableMapServer.count++;
        System.out.println("connection?"+key);
        ServerStreamObserver observer = new ServerStreamObserver(key, this);
        this.connectionMap.put(key,responseObserver);
        System.out.println("putting"+key);
        return observer;
    }

    @Override
    public void getShareableMap(ShareableProto.RequestKeys request, StreamObserver<ShareableProto.StreamableMapObject> responseObserver) {
        System.out.println("got get request");
        ShareableProto.StreamableMapObject.Builder builder = ShareableProto.StreamableMapObject.newBuilder();

        Iterator<Map.Entry<String, String>> itr = this.shareableMap.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<String, String> current = itr.next();
            builder.addStreamableMap(ShareableProto.StreamableMapUpdate.newBuilder().setKey(current.getKey()).setValue(current.getValue()).build());
        }

        responseObserver.onNext(builder.build());
    }
}

class ServerStreamObserver implements StreamObserver<ShareableProto.StreamableMapUpdate> {

    Object key;
    ShareableMapServer shareableMapServer;

    public ServerStreamObserver(int key, ShareableMapServer shareableMapServer) {
        this.key = key;
        this.shareableMapServer = shareableMapServer;
    }

    @Override
    public void onNext(ShareableProto.StreamableMapUpdate streamableMapUpdate) {
        System.out.println("k="+streamableMapUpdate.getKey()+",v="+ streamableMapUpdate.getValue());
//        this.shareableMapServer.shareableMap.put(streamableMapUpdate.getKey(), streamableMapUpdate.getValue());
        this.shareableMapServer.relayShareableMapServer(this.key, streamableMapUpdate);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Remove.!");
        this.shareableMapServer.connectionMap.remove(this.key);
    }

    @Override
    public void onCompleted() {
        System.out.println("onCompleted.!");
        this.shareableMapServer.connectionMap.remove(this.key);
    }
}


package shareable;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ShareableMapServer extends ShareableMapServiceGrpc.ShareableMapServiceImplBase implements ShareableMap {

    public static int count = 0;
    ConcurrentHashMap<String, String> shareableMap;
    HashMap<Object, ServerStreamObserver> connectionMap;

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

    @Override
    public void setValue(String key, String value) {
        this.shareableMap.put(key, value);
    }

    @Override
    public String getValue(String key) {
        return this.shareableMap.get(key);
    }

    public void relayShareableMapServer(Object key, ShareableProto.StreamableMapUpdate streamableMapUpdate) {
        for (Map.Entry<Object, ServerStreamObserver> me : this.connectionMap.entrySet()) {
            if (key == me.getKey()) {
                System.out.println("skipping " + key);
                continue;
            }
            (me.getValue()).onNext(streamableMapUpdate);
        }
    }

    @Override
    public StreamObserver<ShareableProto.StreamableMapUpdate> streamShareableMap(StreamObserver<ShareableProto.StreamableMapUpdate> responseObserver) {
        int key = ShareableMapServer.count++;
        return new ServerStreamObserver(key, this);
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
        this.shareableMapServer.shareableMap.put(streamableMapUpdate.getKey(), streamableMapUpdate.getValue());
        this.shareableMapServer.relayShareableMapServer(this.key, streamableMapUpdate);
    }

    @Override
    public void onError(Throwable throwable) {
        this.shareableMapServer.connectionMap.remove(this.key);
    }

    @Override
    public void onCompleted() {
        this.shareableMapServer.connectionMap.remove(this.key);
    }
}


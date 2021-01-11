package game;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class GameServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("init server");
        Server server = ServerBuilder.forPort(99).addService(new ServerNetworkHandle()).build().start();
        System.out.println("running server");
        server.awaitTermination();
        System.out.println("ended server");
    }
}

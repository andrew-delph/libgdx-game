package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import modules.App;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class GameServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("init server");
        Injector injector = Guice.createInjector(new App());
        ServerNetworkHandle server = injector.getInstance(ServerNetworkHandle.class);
        server.awaitTermination();
        System.out.println("ended server");
    }
}

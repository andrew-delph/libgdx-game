package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import game.GameClient;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import modules.App;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class GameClientMain {
    public static void main (String[] arg) throws InterruptedException, IOException {

//        System.out.println("init server");
//        Server server = ServerBuilder.forPort(99).addService(new ServerNetworkHandle()).build().start();
//        System.out.println("running server");
//
//        System.out.println("ended server");


        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Injector injector = Guice.createInjector(
                new App()
        );

        new LwjglApplication(injector.getInstance(GameClient.class), config);

//        server.awaitTermination();
    }
}

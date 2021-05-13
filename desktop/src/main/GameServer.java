package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.ServerApp;
import networking.server.ServerNetworkHandle;

import java.io.IOException;

public class GameServer {
  public static void main(String[] args) throws InterruptedException, IOException {
    System.out.println("init server");
    Injector injector = Guice.createInjector(new ServerApp());
    ServerNetworkHandle server = injector.getInstance(ServerNetworkHandle.class);
    server.start();
    server.awaitTermination();
    System.out.println("ended server");
  }
}

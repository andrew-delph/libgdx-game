package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import app.screen.GameScreen;
import networking.client.ClientNetworkHandle;

public class ClientGameMain {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new ClientConfig());
    GameScreen gameScreen = injector.getInstance(GameScreen.class);
    ClientNetworkHandle clientNetworkHandle = injector.getInstance(ClientNetworkHandle.class);

    if (args.length > 0) {
      String address = args[0];
      System.out.println("setting address to: " + address);
      String host = address.split(":")[0];
      int port = Integer.parseInt(address.split(":")[1]);
      clientNetworkHandle.setHost(host);
      clientNetworkHandle.setPort(port);
    }

    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    config.height = 500;
    config.width = 500;

    new LwjglApplication(gameScreen, config);
  }
}

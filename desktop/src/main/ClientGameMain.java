package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import configuration.MainConfig;
import infra.app.GameScreen;

public class ClientGameMain {
  public static void main(String[] arg) {

    Injector injector = Guice.createInjector(new ClientConfig());

    GameScreen gameScreen = injector.getInstance(GameScreen.class);

    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    config.height = 500;
    config.width = 500;

    new LwjglApplication(gameScreen, config);
  }
}

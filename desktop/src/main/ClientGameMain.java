package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.screen.GameScreen;
import core.configuration.ClientConfig;

public class ClientGameMain {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new ClientConfig());
    GameScreen gameScreen = injector.getInstance(GameScreen.class);


    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    config.height = 500;
    config.width = 500;

    new LwjglApplication(gameScreen, config);
  }
}

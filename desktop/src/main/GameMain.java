package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.MainConfig;
import infra.app.GameScreen;

public class GameMain {
  public static void main(String[] arg) {

    Injector injector = Guice.createInjector(new MainConfig());

    GameScreen gameScreen = injector.getInstance(GameScreen.class);

    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    new LwjglApplication(gameScreen, config);
  }
}

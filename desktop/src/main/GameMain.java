package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.screen.GameScreen;
import core.configuration.StandAloneConfig;

public class GameMain {
  public static void main(String[] arg) {
    Injector injector = Guice.createInjector(new StandAloneConfig());
    GameScreen gameScreen = injector.getInstance(GameScreen.class);
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    float ratio = 0.7f;
    float size = 1700;
    //    config.fullscreen = true;
    config.height = (int) (size * ratio);
    config.width = (int) size;
    new LwjglApplication(gameScreen, config);
  }
}

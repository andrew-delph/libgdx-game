package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.DesktopApp;
import game.GameClient;

public class GameClientMain {
    public static void main(String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Injector injector = Guice.createInjector(
                new DesktopApp()
        );

        new LwjglApplication(injector.getInstance(GameClient.class), config);
    }
}

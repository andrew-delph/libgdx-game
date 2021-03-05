package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import game.GameClient;
import modules.App;

import java.io.IOException;

public class GameClientMain {
    public static void main(String[] arg) throws InterruptedException, IOException {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Injector injector = Guice.createInjector(
                new App()
        );

        new LwjglApplication(injector.getInstance(GameClient.class), config);
    }
}

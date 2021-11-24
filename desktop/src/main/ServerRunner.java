package main;

import app.Game;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.BaseServerConfig;
import configuration.MainBaseServerConfig;

import java.io.IOException;

public class ServerRunner {

    public static void main(String [] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new MainBaseServerConfig());
        Game game = injector.getInstance(Game.class);
        game.start();

        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}

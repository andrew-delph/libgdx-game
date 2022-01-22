package main;

import app.game.Game;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.exceptions.SerializationDataMissing;
import configuration.MainBaseServerConfig;

import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) throws IOException, InterruptedException, SerializationDataMissing {
        Injector injector = Guice.createInjector(new MainBaseServerConfig());
        Game game = injector.getInstance(Game.class);
        game.start();

        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}

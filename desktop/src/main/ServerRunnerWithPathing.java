package main;

import app.game.Game;
import chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import configuration.MainBaseServerConfig;
import java.io.IOException;

public class ServerRunnerWithPathing {

  public static void main(String[] args)
      throws IOException, InterruptedException, SerializationDataMissing, WrongVersion,
          BodyNotFound {
    Injector injector = Guice.createInjector(new MainBaseServerConfig());

    HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
    HeadlessApplication app = new HeadlessApplication(new ApplicationAdapter() {}, conf);

    Game game = injector.getInstance(Game.class);
    game.start();

    while (true) {
      Thread.sleep(Long.MAX_VALUE);
    }
  }
}

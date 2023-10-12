package main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.game.Game;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.MainBaseServerConfig;
import java.io.IOException;

public class ServerRunnerWithPathing {

  public static void main(String[] args)
      throws IOException,
          InterruptedException,
          SerializationDataMissing,
          WrongVersion,
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

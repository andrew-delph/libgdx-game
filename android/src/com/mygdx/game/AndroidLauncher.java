package com.mygdx.game;

import android.os.Bundle;
import core.app.screen.GameScreen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.configuration.StandAloneConfig;

public class AndroidLauncher extends AndroidApplication {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Injector injector = Guice.createInjector(new StandAloneConfig());
    GameScreen gameScreen = injector.getInstance(GameScreen.class);
    initialize(gameScreen, new AndroidApplicationConfiguration());
  }
}

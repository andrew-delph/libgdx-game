package com.mygdx.game;

import android.os.Bundle;
import app.screen.GameScreen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;

public class AndroidLauncher extends AndroidApplication {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Injector injector = Guice.createInjector(new ClientConfig());
    GameScreen gameScreen = injector.getInstance(GameScreen.class);
    initialize(gameScreen, new AndroidApplicationConfiguration());
  }
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.screen.GameScreen;
import core.configuration.StandAloneConfig;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate {

  @Override
  protected IOSApplication createApplication() {
    IOSApplicationConfiguration config = new IOSApplicationConfiguration();

    Injector injector = Guice.createInjector(new StandAloneConfig());

    GameScreen gameScreen = injector.getInstance(GameScreen.class);

//    ManagedChannelBuilder.forAddress("lala", 1)
//        .usePlaintext()
//        .build();

    ApplicationAdapter applicationAdapter =
        new ApplicationAdapter() {
          @Override
          public void render() {
            super.render();
            System.out.println("render");
//            MyConsumer myConsumer = x -> System.out.println(x + "lala");
//            myConsumer.run("thisOKOKOOK");
          }
        };

    return new IOSApplication(applicationAdapter, config);
  }

  public static void main(String[] argv) {
    NSAutoreleasePool pool = new NSAutoreleasePool();
    UIApplication.main(argv, null, IOSLauncher.class);
    pool.close();
  }
}

package util.mock;

import static org.junit.Assert.assertTrue;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.Gdx;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {

  @Test
  public void badlogicLogoFileExists() {
    System.out.println("start");
    Injector clientInjector = Guice.createInjector(new ClientConfig());

    BaseAssetManager assetManager = clientInjector.getInstance(BaseAssetManager.class);
    assetManager.init();
    assertTrue(
        "This test will only pass when the badlogic.jpg file coming with a new project setup has"
            + " not been deleted.",
        Gdx.files.internal("badlogic.jpg").exists());
  }
}

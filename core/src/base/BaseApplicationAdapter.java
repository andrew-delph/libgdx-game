package base;

import com.badlogic.gdx.ApplicationAdapter;
import com.google.inject.Inject;

public class BaseApplicationAdapter extends ApplicationAdapter {

  public BaseCamera camera;
  public BaseAssetManager assetManager;

  @Inject
  public BaseApplicationAdapter(BaseAssetManager assetManager, BaseCamera camera) {
    this.assetManager = assetManager;
    this.camera = camera;
  }

  public void init() {
    this.assetManager.init();
    this.camera.init();
  }
}

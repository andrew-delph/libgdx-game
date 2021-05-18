package old.base;

import com.badlogic.gdx.graphics.Texture;

public class DesktopAssetManager extends BaseAssetManager {
  public DesktopAssetManager() {}

  public void init() {
    this.load("badlogic.jpg", Texture.class);
    this.load("frog.png", Texture.class);
    this.load("dirtblock.jpg", Texture.class);
    this.update();
    this.finishLoading();
  }
}

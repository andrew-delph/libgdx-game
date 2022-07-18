package app.screen.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;

public class BaseAssetManager extends AssetManager {
  Boolean loaded = false;

  @Inject
  public BaseAssetManager() {}

  public void init() {
    this.load("badlogic.jpg", Texture.class);
    this.load("frog.png", Texture.class);
    this.load("dirtblock.jpg", Texture.class);
    this.load("stone.png", Texture.class);
    this.load("sky.png", Texture.class);
    this.load("dirty.png", Texture.class);
    this.load("ladder.png", Texture.class);
    this.load("bullet.png", Texture.class);
    this.load("turret.png", Texture.class);
    this.load("orb.png", Texture.class);
    this.load("sprite-animation4.png", Texture.class);
    this.update();
    this.finishLoading();
    this.loaded = true;
  }
}

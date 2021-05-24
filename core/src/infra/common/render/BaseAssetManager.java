package infra.common.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class BaseAssetManager extends AssetManager {
    public BaseAssetManager() {
    System.out.println("create BaseAssetManager");
    }

    Boolean loaded = false;

    public void init() {
        this.load("badlogic.jpg", Texture.class);
        this.load("frog.png", Texture.class);
        this.load("dirtblock.jpg", Texture.class);
        this.load("stone.png", Texture.class);
        this.load("sky.png", Texture.class);
        this.update();
        this.finishLoading();
        this.loaded = true;
    }
}

package base;

import com.badlogic.gdx.graphics.Texture;

public class DesktopAssetManager extends BaseAssetManager {
    public DesktopAssetManager() {

    }

    public void init() {
        this.load("badlogic.jpg", Texture.class);
        this.update();
        this.finishLoading();
    }
}

package base;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class BaseCamera extends OrthographicCamera {
  public void init() {
    this.setToOrtho(false, 1280, 720);
    this.update();
  }
}

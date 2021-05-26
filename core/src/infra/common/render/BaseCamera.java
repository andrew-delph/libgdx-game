package infra.common.render;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class BaseCamera extends OrthographicCamera {
  public void init() {
    this.setToOrtho(false, 500, 500);
    this.update();
  }
}

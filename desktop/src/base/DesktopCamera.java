package base;

public class DesktopCamera extends BaseCamera {
  public DesktopCamera() {}

  public void init() {
    this.setToOrtho(false, 1280, 720);
    this.update();
  }
}

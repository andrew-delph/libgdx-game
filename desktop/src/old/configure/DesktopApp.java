package old.configure;

import old.base.BaseAssetManager;
import old.base.BaseCamera;
import old.base.DesktopAssetManager;
import old.base.DesktopCamera;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class DesktopApp extends CoreApp {

  @Override
  protected void configure() {
    super.configure();
    bind(BaseAssetManager.class).to(DesktopAssetManager.class).in(Singleton.class);
    bind(BaseCamera.class).to(DesktopCamera.class);
    bind(Boolean.class).annotatedWith(Names.named("provideTexture")).toInstance(true);
    bind(Integer.class).annotatedWith(Names.named("CoordinateScale")).toInstance(20);
  }
}

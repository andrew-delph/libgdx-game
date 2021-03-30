package configure;

import base.BaseAssetManager;
import base.BaseCamera;
import base.DesktopAssetManager;
import base.DesktopCamera;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class DesktopApp extends CoreApp {

    @Override
    protected void configure() {
        super.configure();
        bind(BaseAssetManager.class).to(DesktopAssetManager.class).in(Singleton.class);
        bind(BaseCamera.class).to(DesktopCamera.class);
        bind(Boolean.class).annotatedWith(Names.named("provideTexture")).toInstance(true);
        bind(Integer.class).annotatedWith(Names.named("CoordinateScale")).toInstance(40);
    }
}

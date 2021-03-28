package configure;

import base.BaseAssetManager;
import base.BaseCamera;
import base.DesktopAssetManager;
import base.DesktopCamera;

public class DesktopApp extends CoreApp {

    @Override
    protected void configure() {
        super.configure();
        bind(BaseAssetManager.class).to(DesktopAssetManager.class);
        bind(BaseCamera.class).to(DesktopCamera.class);
    }
}

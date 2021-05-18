package old.configure;

import com.google.inject.name.Names;

public abstract class TestApp extends CoreApp {

  @Override
  protected void configure() {
    super.configure();
    bind(Boolean.class).annotatedWith(Names.named("provideTexture")).toInstance(false);
    bind(Integer.class).annotatedWith(Names.named("CoordinateScale")).toInstance(15);
  }
}

package configuration;

import com.google.inject.AbstractModule;

public class TestConfig extends AbstractModule {
  @Override
  protected void configure() {
    bind(TestImport.class).asEagerSingleton();
  }
}

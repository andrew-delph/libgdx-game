package configuration;

import com.google.inject.Inject;

public class TestImport {
  @Inject
  TestImport() {}

  public void print() {
    System.out.println("hello");
  }
}

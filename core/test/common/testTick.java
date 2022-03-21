package common;

import org.junit.Test;

public class testTick {
  @Test
  public void testTickCompare() {
    Tick lower = new Tick(2);
    Tick upper = new Tick(5);

    assert lower.compareTo(upper) < 0;
    assert upper.compareTo(lower) > 0;
    assert upper.compareTo(upper) == 0;
    assert lower.compareTo(lower) == 0;
  }
}

package core.common;

import java.util.HashSet;
import java.util.Set;
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

  @Test
  public void testTickHash() {
    Set<Tick> set = new HashSet<>();

    set.add(new Tick(1));
    set.add(new Tick(1));
    assert set.size() == 1;

    set.add(new Tick(2));
    assert set.size() == 2;
  }

  @Test
  public void testTickEqual() {
    assert (new Tick(1)).equals(new Tick(1));
    assert !(new Tick(1)).equals(new Tick(2));
  }
}

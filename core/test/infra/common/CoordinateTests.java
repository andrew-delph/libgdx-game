package infra.common;

import org.junit.Test;

import java.util.HashSet;

public class CoordinateTests {
  @Test
  public void hashTests() {
    HashSet set = new HashSet();
    Coordinate c1 = new Coordinate(1, 2);
    Coordinate c2 = new Coordinate(1, 2);

    set.add(c1);
    assert set.size() == 1;

    set.add(c2);
    assert set.size() == 1;
  }

  @Test
  public void negative() {
    Coordinate c1 = new Coordinate(-11, -22);
    System.out.println(c1.tostring());
  }
}

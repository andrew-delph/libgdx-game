package infra.common;

import org.junit.Test;

public class testCoordinates {

  @Test
  public void testInRange() {
    assert Coordinates.inRange(
        new Coordinates(-1, -1), new Coordinates(1, 1), new Coordinates(0, 0));
    assert Coordinates.inRange(
        new Coordinates(-1, -1), new Coordinates(0, 0), new Coordinates(0, 0));
    assert !Coordinates.inRange(
        new Coordinates(-1, -1), new Coordinates(0, 0), new Coordinates(1, 0));
    assert Coordinates.inRange(new Coordinates(0, 0), new Coordinates(0, 0), new Coordinates(0, 0));
  }
}

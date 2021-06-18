package infra.common;

import org.junit.Test;

public class testCoordinates {

  @Test
  public void testInRange() {
    assert Coordinates.isInRange(
        new Coordinates(-1, -1), new Coordinates(1, 1), new Coordinates(0, 0));
    assert Coordinates.isInRange(
        new Coordinates(-1, -1), new Coordinates(0, 0), new Coordinates(0, 0));
    assert !Coordinates.isInRange(
        new Coordinates(-1, -1), new Coordinates(0, 0), new Coordinates(1, 0));
    assert Coordinates.isInRange(
        new Coordinates(0, 0), new Coordinates(0, 0), new Coordinates(0, 0));
  }

  @Test
  public void testGetInRangeList() {
    assert Coordinates.getInRangeList(new Coordinates(0, 0), new Coordinates(0, 0)).size() == 1;
    assert Coordinates.getInRangeList(new Coordinates(0, 0), new Coordinates(1, 1)).size() == 4;
    assert Coordinates.getInRangeList(new Coordinates(0, 0), new Coordinates(2, 2)).size() == 9;
  }
}
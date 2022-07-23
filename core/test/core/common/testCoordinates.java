package core.common;

import org.junit.Test;

public class testCoordinates {

  @Test
  public void testInRange() {
    assert Coordinates.isInRange(
        CommonFactory.createCoordinates(-1, -1),
        CommonFactory.createCoordinates(1, 1),
        CommonFactory.createCoordinates(0, 0));
    assert Coordinates.isInRange(
        CommonFactory.createCoordinates(-1, -1),
        CommonFactory.createCoordinates(0, 0),
        CommonFactory.createCoordinates(0, 0));
    assert !Coordinates.isInRange(
        CommonFactory.createCoordinates(-1, -1),
        CommonFactory.createCoordinates(0, 0),
        CommonFactory.createCoordinates(1, 0));
    assert Coordinates.isInRange(
        CommonFactory.createCoordinates(0, 0),
        CommonFactory.createCoordinates(0, 0),
        CommonFactory.createCoordinates(0, 0));
  }

  @Test
  public void testGetInRangeList() {
    assert Coordinates.getInRangeList(
                CommonFactory.createCoordinates(0, 0), CommonFactory.createCoordinates(0, 0))
            .size()
        == 1;
    assert Coordinates.getInRangeList(
                CommonFactory.createCoordinates(0, 0), CommonFactory.createCoordinates(1, 1))
            .size()
        == 4;
    assert Coordinates.getInRangeList(
                CommonFactory.createCoordinates(0, 0), CommonFactory.createCoordinates(2, 2))
            .size()
        == 9;
  }

  @Test
  public void testCalcDistance() {
    Coordinates c1 = CommonFactory.createCoordinates(0, 1);
    Coordinates c2 = CommonFactory.createCoordinates(0, 2);
    assert c1.calcDistance(c2) == 1;
  }
}

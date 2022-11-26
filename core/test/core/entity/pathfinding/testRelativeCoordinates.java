package core.entity.pathfinding;

import core.common.CommonFactory;
import org.junit.Test;

public class testRelativeCoordinates {
  @Test
  public void testRelativeCoordinates() {
    RelativeCoordinates relativeCoordinates = new RelativeCoordinates(0, 0);
    assert relativeCoordinates
        .applyRelativeCoordinates(CommonFactory.createCoordinates(0, 0))
        .equals(CommonFactory.createCoordinates(0, 0));

    relativeCoordinates = new RelativeCoordinates(1, 0);
    assert relativeCoordinates
        .applyRelativeCoordinates(CommonFactory.createCoordinates(0, 0))
        .equals(CommonFactory.createCoordinates(1, 0));

    relativeCoordinates = new RelativeCoordinates(0, 3);
    assert relativeCoordinates
        .applyRelativeCoordinates(CommonFactory.createCoordinates(0, 0))
        .equals(CommonFactory.createCoordinates(0, 3));
  }
}

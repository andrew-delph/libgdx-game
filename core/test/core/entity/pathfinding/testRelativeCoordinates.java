package core.entity.pathfinding;

import core.entity.attributes.msc.Coordinates;
import core.entity.pathfinding.RelativeCoordinates;
import org.junit.Test;

public class testRelativeCoordinates {
  @Test
  public void testRelativeCoordinates() {
    RelativeCoordinates relativeCoordinates = new RelativeCoordinates(0, 0);
    assert relativeCoordinates
        .applyRelativeCoordinates(new Coordinates(0, 0))
        .equals(new Coordinates(0, 0));

    relativeCoordinates = new RelativeCoordinates(1, 0);
    assert relativeCoordinates
        .applyRelativeCoordinates(new Coordinates(0, 0))
        .equals(new Coordinates(1, 0));

    relativeCoordinates = new RelativeCoordinates(0, 3);
    assert relativeCoordinates
        .applyRelativeCoordinates(new Coordinates(0, 0))
        .equals(new Coordinates(0, 3));
  }
}

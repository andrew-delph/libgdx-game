package core.common;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

public class testUtil {

  @Test
  public void testCalcVelocity() {

    Coordinates start;
    Coordinates end;
    Vector2 velocity;

    start = CommonFactory.createCoordinates(0, 0);
    end = CommonFactory.createCoordinates(10, 10);
    velocity = Util.calcVelocity(start, end, 2);
    System.out.println(velocity);
    assert velocity.x > 0;
    assert velocity.y > 0;

    start = CommonFactory.createCoordinates(0, 0);
    end = CommonFactory.createCoordinates(-10, 10);
    velocity = Util.calcVelocity(start, end, 2);
    System.out.println(velocity);
    assert velocity.x < 0;
    assert velocity.y > 0;

    start = CommonFactory.createCoordinates(0, 0);
    end = CommonFactory.createCoordinates(-10, -10);
    velocity = Util.calcVelocity(start, end, 2);
    System.out.println(velocity);
    assert velocity.x < 0;
    assert velocity.y < 0;

    start = CommonFactory.createCoordinates(0, 0);
    end = CommonFactory.createCoordinates(10, -10);
    velocity = Util.calcVelocity(start, end, 2);
    System.out.println(velocity);
    assert velocity.x > 0;
    assert velocity.y < 0;
  }
}

package core.common;

import com.badlogic.gdx.math.Vector2;

public class Util {

  public static Vector2 calcVelocity(Coordinates start, Coordinates end, float power) {
    double c = power;
    double a, b, theta;
    double x = end.getXReal() - start.getXReal();
    double y = end.getYReal() - start.getYReal();
    // need to get theta; tanΘ = (opposite/adjacent)
    theta = Math.atan(y / x);
    // get a; cosΘ = (adjacent/hypotenuse)
    a = Math.cos(theta) * c;
    // get b; sinΘ = (opposite/hypotenuse)
    b = Math.sin(theta) * c;

    if (x > 0) a = Math.abs(a);
    else a = Math.abs(a) * -1;

    if (y > 0) b = Math.abs(b);
    else b = Math.abs(b) * -1;

    return new Vector2((float) a, (float) b);
  }

  public static int calcTicksFromUnits(int hours, int minutes, int seconds) {
    return calcTicksFromHours(hours)
        + calcTicksFromMinutes(minutes)
        + calcTicksFromSeconds(seconds);
  }

  public static int calcTicksFromSeconds(int seconds) {
    return seconds * 1000 / GameSettings.UPDATE_INTERVAL;
  }

  public static int calcTicksFromMinutes(int minutes) {
    return minutes * calcTicksFromSeconds(60);
  }

  public static int calcTicksFromHours(int hours) {
    return hours * calcTicksFromMinutes(60);
  }
}

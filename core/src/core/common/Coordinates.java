package core.common;

import com.badlogic.gdx.math.Vector2;
import core.networking.events.interfaces.SerializeNetworkData;
import core.networking.translation.NetworkDataSerializer;
import java.util.LinkedList;
import java.util.List;
import networking.NetworkObjects.NetworkData;

public class Coordinates implements SerializeNetworkData {
  final float x;
  final float y;

  protected Coordinates(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public static Boolean isInRange(
      Coordinates bottomLeft, Coordinates topRight, Coordinates target) {
    if (target.getX() < bottomLeft.getX()) return false;
    if (target.getX() > topRight.getX()) return false;
    if (target.getY() < bottomLeft.getY()) return false;
    return target.getY() <= topRight.getY();
  }

  public static List<Coordinates> getInRangeList(Coordinates bottomLeft, Coordinates topRight) {
    List<Coordinates> coordinatesList = new LinkedList<>();

    Coordinates topLeftCoordinates =
        CommonFactory.createCoordinates(bottomLeft.getXReal(), topRight.getYReal());

    Coordinates root = bottomLeft;
    Coordinates current;
    while (!root.equals(topLeftCoordinates.getUp())) {
      root = root.getUp();
      current = root;
      Coordinates rowRightCoordinates =
          CommonFactory.createCoordinates(topRight.getXReal(), current.getYReal());
      while (!current.equals(rowRightCoordinates.getRight())) {
        coordinatesList.add(current);
        current = current.getRight();
      }
    }
    return coordinatesList;
  }

  public Coordinates getBase() {
    return CommonFactory.createCoordinates(this.getX(), this.getY());
  }

  public int getX() {
    return (int) Math.floor(this.x);
  }

  public int getY() {
    return (int) Math.floor(this.y);
  }

  public float getXReal() {
    return this.x;
  }

  public float getYReal() {
    return this.y;
  }

  public synchronized Coordinates getUp() {
    return CommonFactory.createCoordinates(this.getX(), this.getY() + 1);
  }

  public synchronized Coordinates getDown() {
    return CommonFactory.createCoordinates(this.getX(), this.getY() - 1);
  }

  public synchronized Coordinates getLeft() {
    return CommonFactory.createCoordinates(this.getX() - 1, this.getY());
  }

  public synchronized Coordinates getRight() {
    return CommonFactory.createCoordinates(this.getX() + 1, this.getY());
  }

  public synchronized Coordinates getMiddle() {
    return CommonFactory.createCoordinates(this.getX() + 0.5f, this.getY() + 0.5f);
  }

  public synchronized Coordinates add(float x, float y) {
    return CommonFactory.createCoordinates(this.getXReal() + x, this.getYReal() + y);
  }

  public double calcDistance(Coordinates other) {
    return Math.sqrt(
        Math.pow(this.getXReal() - other.getXReal(), 2)
            + Math.pow(this.getYReal() - other.getYReal(), 2));
  }

  public double[] calcDifference(Coordinates other) {
    return new double[] {other.getXReal() - this.getXReal(), other.getYReal() - this.getYReal()};
  }

  @Override
  public int hashCode() {
    return (this.x + "," + this.y).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Coordinates other = (Coordinates) obj;
    return x == other.x && y == other.y;
  }

  public String toString() {
    return this.getXReal() + "," + this.getYReal();
  }

  public Vector2 toPhysicsVector2() {
    return new Vector2(
        this.getXReal() * GameSettings.PHYSICS_SCALE, this.getYReal() * GameSettings.PHYSICS_SCALE);
  }

  public Vector2 toRenderVector2() {
    return new Vector2(
        this.getXReal() * GameSettings.PIXEL_SCALE, this.getYReal() * GameSettings.PIXEL_SCALE);
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.createCoordinates(this);
  }
}

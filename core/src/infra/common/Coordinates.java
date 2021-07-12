package infra.common;

import com.badlogic.gdx.math.Vector2;
import infra.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public class Coordinates {
  float x;
  float y;

  public Coordinates(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Coordinates(Vector2 vector2) {
    this.x = vector2.x / Entity.coordinatesScale;
    this.y = vector2.y / Entity.coordinatesScale;
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

    Coordinates topLeftCoordinates = new Coordinates(bottomLeft.getXReal(), topRight.getYReal());

    Coordinates root = bottomLeft;
    Coordinates current;
    while (!root.equals(topLeftCoordinates.getUp())) {
      root = root.getUp();
      current = root;
      Coordinates rowRightCoordinates = new Coordinates(topRight.getXReal(), current.getYReal());
      while (!current.equals(rowRightCoordinates.getRight())) {
        coordinatesList.add(current);
        current = current.getRight();
      }
    }
    return coordinatesList;
  }

  public Coordinates getBase() {
    return new Coordinates(this.getX(), this.getY());
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
    return new Coordinates(this.getX(), this.getY() + 1);
  }

  public synchronized Coordinates getDown() {
    return new Coordinates(this.getX(), this.getY() - 1);
  }

  public synchronized Coordinates getLeft() {
    return new Coordinates(this.getX() - 1, this.getY());
  }

  public synchronized Coordinates getRight() {
    return new Coordinates(this.getX() + 1, this.getY());
  }

  public synchronized Coordinates getMiddle() {
    return new Coordinates(this.getX() + 0.5f, this.getY() + 0.5f);
  }

  public double calcDistance(Coordinates other) {
    return Math.sqrt(
        Math.pow(this.getXReal() - other.getXReal(), 2)
            + Math.pow(this.getYReal() - other.getYReal(), 2));
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

  public Vector2 toVector2() {
    return new Vector2(
        this.getXReal() * Entity.coordinatesScale, this.getYReal() * Entity.coordinatesScale);
  }
}

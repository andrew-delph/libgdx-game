package infra.common;

public class Coordinates {
  float x;
  float y;

  public Coordinates(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return (int) this.x;
  }

  public int getY() {
    return (int) this.y;
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

  public static Boolean inRange(Coordinates bottomLeft, Coordinates topRight, Coordinates target) {
    if (target.getX() < bottomLeft.getX()) return false;
    if (target.getX() > topRight.getX()) return false;
    if (target.getY() < bottomLeft.getY()) return false;
    if (target.getY() > topRight.getY()) return false;
    return true;
  }
}

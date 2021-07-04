package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import infra.common.Coordinates;
import infra.entity.Entity;

public class RelativeCoordinates {
  float relativeX;
  float relativeY;

  public RelativeCoordinates(float relativeX, float relativeY) {
    this.relativeX = relativeX;
    this.relativeY = relativeY;
  }

  public float getRelativeX() {
    return relativeX;
  }

  public float getRelativeY() {
    return relativeY;
  }

  public RelativeCoordinates(Vector2 vector2) {
    this.relativeX = vector2.x / Entity.coordinatesScale;
    this.relativeY = vector2.y / Entity.coordinatesScale;
  }

  public Coordinates applyRelativeCoordinates(Coordinates coordinates) {
    return new Coordinates(
        coordinates.getXReal() + this.relativeX, coordinates.getYReal() + this.relativeY);
  }

  public RelativeCoordinates getUp() {
    return new RelativeCoordinates(this.getRelativeX(), this.getRelativeY() + 1);
  }

  public RelativeCoordinates getDown() {
    return new RelativeCoordinates(this.getRelativeX(), this.getRelativeY() - 1);
  }

  public RelativeCoordinates getLeft() {
    return new RelativeCoordinates(this.getRelativeX() - 1, this.getRelativeY());
  }

  public RelativeCoordinates getRight() {
    return new RelativeCoordinates(this.getRelativeX() + 1, this.getRelativeY());
  }

  public boolean equalBase(RelativeCoordinates other){
    return (int) this.relativeX == (int) other.relativeX && (int)this.relativeY == (int)this.relativeY;
  }

  @Override
  public String toString() {
    return "RelativeCoordinates{" +
            "relativeX=" + relativeX +
            ", relativeY=" + relativeY +
            '}';
  }
}

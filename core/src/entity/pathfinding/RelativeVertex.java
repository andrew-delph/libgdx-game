package entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import entity.EntityBodyBuilder;

public class RelativeVertex {
  public RelativeCoordinates relativeCoordinates;
  Vector2 velocity;
  EntityStructure entityStructure;

  @Inject EntityBodyBuilder entityBodyBuilder;
  World world;
  Body body;

  public RelativeVertex(
      EntityStructure entityStructure, RelativeCoordinates relativeCoordinates, Vector2 velocity) {
    this.entityStructure = entityStructure;
    this.relativeCoordinates = relativeCoordinates;
    this.velocity = velocity;
  }

  @Override
  public String toString() {
    return "RelativeVertex{" + "relativeCoordinates=" + relativeCoordinates + '}';
  }

  @Override
  public int hashCode() {
    return (this.relativeCoordinates.hashCode() + ",").hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    RelativeVertex other = (RelativeVertex) obj;
    return this.relativeCoordinates.equals(other.relativeCoordinates)
        && this.velocity.equals(other.velocity);
  }
}

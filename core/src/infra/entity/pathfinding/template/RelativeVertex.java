package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import infra.entity.EntityBodyBuilder;

public class RelativeVertex {
  public RelativeCoordinates relativeCoordinates;
  Vector2 velocity;
  BlockStructure blockStructure;

  @Inject EntityBodyBuilder entityBodyBuilder;
  World world;
  Body body;

  public RelativeVertex(
      BlockStructure blockStructure, RelativeCoordinates relativeCoordinates, Vector2 velocity) {
    this.blockStructure = blockStructure;
    this.relativeCoordinates = relativeCoordinates;
    this.velocity = velocity;
  }

  @Override
  public String toString() {
    return "RelativeVertex{" + "relativeCoordinates=" + relativeCoordinates + '}';
  }
}

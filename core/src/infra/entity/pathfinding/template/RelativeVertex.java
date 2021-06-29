package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.entity.EntityBodyBuilder;
import infra.entity.block.Block;
import infra.entity.block.SolidBlock;

import java.util.Map;

public class RelativeVertex {
  RelativeCoordinates relativeCoordinates;
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

}

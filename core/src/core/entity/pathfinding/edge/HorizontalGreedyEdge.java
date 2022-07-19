package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import core.chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.entity.attributes.msc.Coordinates;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.RelativeCoordinates;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;
import core.entity.Entity;

public class HorizontalGreedyEdge extends AbstractEdge {
  RelativeCoordinates currentRelativeCoordinates;

  public HorizontalGreedyEdge(
      EntityStructure entityStructure, RelativeVertex from, RelativeVertex to) {
    super(entityStructure, from, to);
    this.currentRelativeCoordinates = from.getRelativeCoordinates();
  }

  @Override
  public Coordinates applyTransition(Coordinates sourceCoordinates) {
    return super.applyTransition(sourceCoordinates).getBase();
  }

  @Override
  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new HorizontalEdgeStepper();
  }

  @Override
  public void render(Coordinates position) {
    pathDebugRender.setColor(Color.GREEN);
    super.render(position);
  }
}

class HorizontalEdgeStepper extends EdgeStepper {

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode)
      throws EdgeStepperException, ChunkNotFound, BodyNotFound {
    String actionKey;

    if (!entity.coordinates.getBase().equals(relativePathNode.startPosition.getBase().getDown())
        && !entity.coordinates.getBase().equals(relativePathNode.startPosition.getBase())
        && !entity.coordinates.getBase().equals(relativePathNode.getEndPosition().getBase())) {
      throw new EdgeStepperException("not on track");
    }

    if (relativePathNode.getEndPosition().calcDistance(entity.coordinates) < 0.3) {
      Vector2 setBodyPosition = relativePathNode.getEndPosition().toPhysicsVector2();
      entity.setBodyPosition(setBodyPosition);
      this.finish();
      return;
    }

    if (relativePathNode.getEndPosition().getXReal() + 0.1 > entity.coordinates.getXReal()) {
      actionKey = "right";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    } else if (relativePathNode.getEndPosition().getXReal() < entity.coordinates.getXReal()) {
      actionKey = "left";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    }
    if (relativePathNode.getEndPosition().getYReal() > entity.coordinates.getYReal()) {
      actionKey = "climbUp";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    } else if (relativePathNode.getEndPosition().getYReal() < entity.coordinates.getYReal() - 0.1) {
      actionKey = "climbDown";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    }
  }
}

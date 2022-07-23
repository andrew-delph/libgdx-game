package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.entity.Entity;
import core.common.Coordinates;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.RelativeCoordinates;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;

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

    if (!entity
            .getCoordinatesWrapper()
            .getCoordinates()
            .getBase()
            .equals(relativePathNode.startPosition.getBase().getDown())
        && !entity
            .getCoordinatesWrapper()
            .getCoordinates()
            .getBase()
            .equals(relativePathNode.startPosition.getBase())
        && !entity
            .getCoordinatesWrapper()
            .getCoordinates()
            .getBase()
            .equals(relativePathNode.getEndPosition().getBase())) {
      throw new EdgeStepperException("not on track");
    }

    if (relativePathNode
            .getEndPosition()
            .calcDistance(entity.getCoordinatesWrapper().getCoordinates())
        < 0.3) {
      Vector2 setBodyPosition = relativePathNode.getEndPosition().toPhysicsVector2();
      entity.setBodyPosition(setBodyPosition);
      this.finish();
      return;
    }

    if (relativePathNode.getEndPosition().getXReal() + 0.1
        > entity.getCoordinatesWrapper().getCoordinates().getXReal()) {
      actionKey = "right";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    } else if (relativePathNode.getEndPosition().getXReal()
        < entity.getCoordinatesWrapper().getCoordinates().getXReal()) {
      actionKey = "left";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    }
    if (relativePathNode.getEndPosition().getYReal()
        > entity.getCoordinatesWrapper().getCoordinates().getYReal()) {
      actionKey = "climbUp";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    } else if (relativePathNode.getEndPosition().getYReal()
        < entity.getCoordinatesWrapper().getCoordinates().getYReal() - 0.1) {
      actionKey = "climbDown";
      if (entity.getEntityController().isActionValid(actionKey, entity)) {
        entity.getEntityController().applyAction(actionKey, entity);
      }
    }
  }
}

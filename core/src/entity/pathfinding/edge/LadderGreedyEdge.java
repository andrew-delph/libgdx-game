package entity.pathfinding.edge;

import static app.screen.GameScreen.pathDebugRender;

import app.GameController;
import chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.graphics.Color;
import common.exceptions.ChunkNotFound;
import common.exceptions.EdgeStepperException;
import entity.Entity;
import entity.attributes.Coordinates;
import entity.misc.Ladder;
import entity.pathfinding.EntityStructure;
import entity.pathfinding.PathGameStoreOverride;
import entity.pathfinding.RelativePathNode;
import entity.pathfinding.RelativeVertex;

public class LadderGreedyEdge extends HorizontalGreedyEdge {
  GameController gameController;

  public LadderGreedyEdge(
      GameController gameController,
      EntityStructure entityStructure,
      RelativeVertex from,
      RelativeVertex to) {
    super(entityStructure, from, to);
    this.gameController = gameController;
  }

  @Override
  public void render(Coordinates position) {
    pathDebugRender.setColor(Color.BLUE);
    super.render(position);
  }

  @Override
  public double getCost() {
    return 2;
  }

  @Override
  public void appendPathGameStoreOverride(
      PathGameStoreOverride pathGameStoreOverride, Coordinates sourceCoordinates) {

    pathGameStoreOverride.registerEntityTypeOverride(
        Ladder.class, this.applyTransition(sourceCoordinates));
  }

  @Override
  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new LadderEdgeStepper(this.gameController);
  }
}

class LadderEdgeStepper extends HorizontalEdgeStepper {
  GameController gameController;

  public LadderEdgeStepper(GameController gameController) {
    this.gameController = gameController;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode)
      throws EdgeStepperException, ChunkNotFound, BodyNotFound {
    this.gameController.createLadder(relativePathNode.getEndPosition());
    super.follow(entity, relativePathNode);
  }
}

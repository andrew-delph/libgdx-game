package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.graphics.Color;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.entity.attributes.msc.Coordinates;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.PathGameStoreOverride;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;
import core.entity.Entity;
import core.entity.misc.Ladder;

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

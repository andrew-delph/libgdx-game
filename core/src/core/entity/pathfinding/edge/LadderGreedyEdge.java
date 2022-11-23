package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import com.badlogic.gdx.graphics.Color;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Coordinates;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.entity.Entity;
import core.entity.block.DirtBlock;
import core.entity.misc.Ladder;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.PathGameStoreOverride;
import core.entity.pathfinding.RelativeCoordinates;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;

public class LadderGreedyEdge extends HorizontalGreedyEdge {
  GameController gameController;
  RelativeCoordinates ladderPlacement;

  public LadderGreedyEdge(
      GameController gameController,
      EntityStructure entityStructure,
      RelativeVertex from,
      RelativeVertex to,
      RelativeCoordinates ladderPlacement,
      String name) {
    super(entityStructure, from, to, name);
    this.gameController = gameController;
    this.ladderPlacement = ladderPlacement;
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
        DirtBlock.class, ladderPlacement.applyRelativeCoordinates(sourceCoordinates));
    pathGameStoreOverride.registerEntityTypeOverride(
        Ladder.class, ladderPlacement.applyRelativeCoordinates(sourceCoordinates));
  }

  @Override
  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new LadderEdgeStepper(this.gameController, this.ladderPlacement);
  }
}

class LadderEdgeStepper extends HorizontalEdgeStepper {
  GameController gameController;
  RelativeCoordinates ladderPlacement;

  public LadderEdgeStepper(GameController gameController, RelativeCoordinates ladderPlacement) {
    this.gameController = gameController;
    this.ladderPlacement = ladderPlacement;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode)
      throws EdgeStepperException, ChunkNotFound, BodyNotFound {
    this.gameController.createLadder(
        ladderPlacement.applyRelativeCoordinates(relativePathNode.getStartPosition()));
    super.follow(entity, relativePathNode);
  }
}

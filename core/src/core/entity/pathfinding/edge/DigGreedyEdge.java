package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import com.badlogic.gdx.graphics.Color;
import core.app.game.GameController;
import core.app.screen.assets.animations.AnimationState;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.block.BlockFactory;
import core.entity.block.SkyBlock;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.PathGameStoreOverride;
import core.entity.pathfinding.RelativeCoordinates;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;

public class DigGreedyEdge extends HorizontalGreedyEdge {
  GameController gameController;
  GameStore gameStore;
  BlockFactory blockFactory;
  RelativeCoordinates digPosition;

  public DigGreedyEdge(
      GameController gameController,
      GameStore gameStore,
      BlockFactory blockFactory,
      EntityStructure entityStructure,
      RelativeVertex position,
      RelativeCoordinates digPosition,
      String name) {
    super(entityStructure, position, position, name);
    this.gameController = gameController;
    this.gameStore = gameStore;
    this.blockFactory = blockFactory;
    this.digPosition = digPosition;
  }

  @Override
  public double getCost() {
    return 3;
  }

  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new DigEdgeStepper(
        this.gameController, this.gameStore, this.blockFactory, this.digPosition);
  }

  @Override
  public void appendPathGameStoreOverride(
      PathGameStoreOverride pathGameStoreOverride, Coordinates sourceCoordinates) {

    pathGameStoreOverride.registerEntityTypeOverride(
        SkyBlock.class, this.digPosition.applyRelativeCoordinates(sourceCoordinates));
  }

  @Override
  public void render(Coordinates position) {
    // TODO change to draw to cover the dig
    if (GameSettings.RENDER_DEBUG) {
      pathDebugRender.setColor(Color.RED);
      pathDebugRender.circle(position.toPhysicsVector2().x, position.toPhysicsVector2().y, 4);
    }
  }
}

class DigEdgeStepper extends HorizontalEdgeStepper {
  GameController gameController;
  GameStore gameStore;
  BlockFactory blockFactory;
  RelativeCoordinates digPosition;

  public DigEdgeStepper(
      GameController gameController,
      GameStore gameStore,
      BlockFactory blockFactory,
      RelativeCoordinates digPosition) {
    this.gameController = gameController;
    this.gameStore = gameStore;
    this.blockFactory = blockFactory;
    this.digPosition = digPosition;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode)
      throws EdgeStepperException, ChunkNotFound, BodyNotFound {

    RelativeCoordinates relativeCoordinatesBase = new RelativeCoordinates(0, 0);

    if (digPosition.equals(relativeCoordinatesBase.getLeft())) {
      entity.getEntityStateMachine().attemptTransition(AnimationState.DIGGING_LEFT);
    } else if (digPosition.equals(relativeCoordinatesBase.getRight())) {
      entity.getEntityStateMachine().attemptTransition(AnimationState.DIGGING_RIGHT);
    } else if (digPosition.equals(relativeCoordinatesBase.getDown())) {
      entity.getEntityStateMachine().attemptTransition(AnimationState.DIGGING_DOWN);
    } else if (digPosition.equals(relativeCoordinatesBase.getUp())) {
      entity.getEntityStateMachine().attemptTransition(AnimationState.DIGGING_UP);
    } else {
      throw new EdgeStepperException("cannot determine the position of the dig");
    }

    try {
      this.gameStore.getBlock(
          this.digPosition.applyRelativeCoordinates(relativePathNode.startPosition));
    } catch (EntityNotFound entityNotFound) {
      this.finish();
    }
  }
}

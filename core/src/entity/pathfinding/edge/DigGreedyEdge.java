package entity.pathfinding.edge;

import app.GameController;
import common.Coordinates;
import common.GameStore;
import entity.Entity;
import entity.block.BlockFactory;
import entity.block.SkyBlock;
import entity.pathfinding.*;

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
      RelativeCoordinates digPosition) {
    super(entityStructure, position, position);
    this.gameController = gameController;
    this.gameStore = gameStore;
    this.blockFactory = blockFactory;
    this.digPosition = digPosition;
  }

  @Override
  public double getCost() {
    return 3;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode) throws Exception {
    this.gameController.replaceBlock(
        this.gameStore.getBlock(
                this.digPosition.applyRelativeCoordinates(relativePathNode.startPosition))
            .uuid,
        blockFactory.createSky());
    super.follow(entity, relativePathNode);
  }

  @Override
  public void appendPathGameStoreOverride(
      PathGameStoreOverride pathGameStoreOverride, Coordinates sourceCoordinates) {

    pathGameStoreOverride.registerEntityTypeOverride(
        SkyBlock.class, this.digPosition.applyRelativeCoordinates(sourceCoordinates));
  }
}

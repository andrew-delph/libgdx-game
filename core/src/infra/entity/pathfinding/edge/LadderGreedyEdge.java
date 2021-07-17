package infra.entity.pathfinding.edge;

import infra.app.GameController;
import infra.common.Coordinates;
import infra.entity.Entity;
import infra.entity.misc.Ladder;
import infra.entity.pathfinding.EntityStructure;
import infra.entity.pathfinding.PathGameStoreOverride;
import infra.entity.pathfinding.RelativePathNode;
import infra.entity.pathfinding.RelativeVertex;

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
  public void follow(Entity entity, RelativePathNode relativePathNode) throws Exception {
    this.gameController.placeLadder(relativePathNode.getEndPosition());
    super.follow(entity, relativePathNode);

    String actionKey;
    if (relativePathNode.getEndPosition().getYReal() > entity.coordinates.getYReal()) {
      actionKey = "climbUp";
    } else if (relativePathNode.getEndPosition().getYReal() < entity.coordinates.getYReal()) {
      actionKey = "climbDown";
    } else return;

    //    if (entity.entityController.getAction(actionKey).isValid(entity.getBody())) {
    //
    //    }
    entity.entityController.applyAction(actionKey, entity.getBody());
  }
}

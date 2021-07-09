package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.entity.EntityBodyBuilder;
import infra.entity.block.Block;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;
import infra.entity.controllers.actions.EntityAction;
import infra.entity.controllers.actions.EntityActionFactory;

import java.util.Map;

public class RelativeActionEdgeGenerator {

  World world;
  Body body;

  @Inject BlockStructureFactory blockStructureFactory;
  @Inject EntityBodyBuilder entityBodyBuilder;

  @Inject EntityActionFactory entityActionFactory;

  @Inject
  RelativeActionEdgeGenerator() {}

  public RelativeActionEdge generateRelativeActionEdge(
      BlockStructure rootBlockStructure, RelativeVertex fromRelativeVertex, String actionKey) {
    this.setupWorld(rootBlockStructure, fromRelativeVertex);

    EntityAction entityAction;

    if (actionKey.equals("left"))
      entityAction = entityActionFactory.createHorizontalMovementAction(-5);
    else if (actionKey.equals("right"))
      entityAction = entityActionFactory.createHorizontalMovementAction(5);
    else if (actionKey.equals("jump"))
      entityAction = entityActionFactory.createJumpMovementAction();
    else if (actionKey.equals("stop"))
      entityAction = entityActionFactory.createStopMovementAction();
    else return null;

    entityAction.apply(this.body);

    this.world.step(1 / 5f, 6, 2);

    BlockStructure newBlockStructure = rootBlockStructure.copy();

    RelativeCoordinates newRelativeCoordinates = new RelativeCoordinates(this.body.getPosition());

    // set empty bottom left
    newBlockStructure.registerRelativeBlock(newRelativeCoordinates, EmptyBlock.class);
    // set empty bottom right
    //    newBlockStructure.registerRelativeBlock(
    //        newRelativeCoordinates.getRight().round(), EmptyBlock.class);
    //    // set empty top left
    //    newBlockStructure.registerRelativeBlock(
    //        newRelativeCoordinates.getUp().round(), EmptyBlock.class);
    //    // set empty top right
    //    newBlockStructure.registerRelativeBlock(
    //        newRelativeCoordinates.getUp().getRight().round(), EmptyBlock.class);

    RelativeVertex toRelativeVertex =
        new RelativeVertex(
            newBlockStructure, newRelativeCoordinates, this.body.getLinearVelocity());

    return new RelativeActionEdge(fromRelativeVertex, toRelativeVertex, actionKey);
  }

  private void setupWorld(BlockStructure blockStructure, RelativeVertex relativeVertex) {
    this.world = new World(new Vector2(0, -1f), false);

    for (Map.Entry<RelativeCoordinates, Class<? extends Block>> relativeBlockMapEntry :
        blockStructure.getRelativeBlockMapEntrySet()) {
      Class<? extends Block> blockClass = relativeBlockMapEntry.getValue();
      RelativeCoordinates blockRelativeCoordinates = relativeBlockMapEntry.getKey();
      if (blockClass.isInstance(SolidBlock.class)) {
        entityBodyBuilder.createSolidBlockBody(
            this.world, blockRelativeCoordinates.applyRelativeCoordinates(new Coordinates(0, 0)));
      }
    }

    this.body =
        entityBodyBuilder.createEntityBody(
            this.world,
            relativeVertex.relativeCoordinates.applyRelativeCoordinates(new Coordinates(0, 0)));
    this.body.setLinearVelocity(relativeVertex.velocity);
  }
}

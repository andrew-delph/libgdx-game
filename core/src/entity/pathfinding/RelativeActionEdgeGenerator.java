package entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import common.Coordinates;
import entity.Entity;
import entity.EntityBodyBuilder;
import entity.block.EmptyBlock;
import entity.block.SolidBlock;
import entity.controllers.actions.EntityAction;
import entity.controllers.actions.EntityActionFactory;

import java.util.Map;

public class RelativeActionEdgeGenerator {

  World world;
  Body body;

  @Inject EntityStructureFactory entityStructureFactory;
  @Inject EntityBodyBuilder entityBodyBuilder;

  @Inject EntityActionFactory entityActionFactory;

  @Inject
  RelativeActionEdgeGenerator() {}

  public RelativeActionEdge generateRelativeActionEdge(
      EntityStructure rootEntityStructure, RelativeVertex fromRelativeVertex, String actionKey) {
    this.setupWorld(rootEntityStructure, fromRelativeVertex);

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

    EntityStructure newEntityStructure = rootEntityStructure.copy();

    Vector2 rootPosition = this.body.getPosition();

    RelativeCoordinates newRelativeCoordinates = new RelativeCoordinates(rootPosition);

    // set empty bottom left
    newEntityStructure.registerRelativeEntity(newRelativeCoordinates, EmptyBlock.class);
    // set empty bottom right
    newEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(rootPosition.cpy().add(Entity.staticWidth, 0)), EmptyBlock.class);
    // set empty top left
    newEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(rootPosition.cpy().add(0, Entity.staticHeight)), EmptyBlock.class);
    // set empty top right
    newEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(rootPosition.cpy().add(Entity.staticWidth, Entity.staticHeight)),
        EmptyBlock.class);

    RelativeVertex toRelativeVertex =
        new RelativeVertex(
            newEntityStructure, newRelativeCoordinates, this.body.getLinearVelocity());

    this.world.dispose();
    System.gc();

    return new RelativeActionEdge(fromRelativeVertex, toRelativeVertex, actionKey);
  }

  private void setupWorld(EntityStructure entityStructure, RelativeVertex relativeVertex) {
    this.world = new World(new Vector2(0, -1f), false);

    for (Map.Entry<RelativeCoordinates, Class<? extends Entity>> relativeBlockMapEntry :
        entityStructure.getRelativeEntityMapEntrySet()) {
      Class<? extends Entity> entityClass = relativeBlockMapEntry.getValue();
      RelativeCoordinates blockRelativeCoordinates = relativeBlockMapEntry.getKey();
      if (entityClass.isInstance(SolidBlock.class)) {
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
package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import infra.app.GameController;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;
import infra.entity.misc.Ladder;
import infra.entity.pathfinding.edge.HorizontalGreedyEdge;
import infra.entity.pathfinding.edge.LadderGreedyEdge;

public class EdgeRegistration {

  @Inject EdgeStore edgeStore;
  @Inject EntityStructureFactory entityStructureFactory;
  @Inject TemplateEdgeGeneratorFactory templateEdgeGeneratorFactory;
  @Inject GameController gameController;

  @Inject
  EdgeRegistration() {}

  public void edgeRegistration() {
    this.horizontalGreedyRegisterEdges();
    this.templateEdgeRegistration();
    this.ladderGreedyRegisterEdges();
  }

  public void templateEdgeRegistration() {

    TemplateEdgeGenerator templateEdgeGenerator =
        templateEdgeGeneratorFactory.create(
            new RelativeVertex(
                entityStructureFactory.createEntityStructure(),
                new RelativeCoordinates(0, 0),
                new Vector2(0, 0)));
    templateEdgeGenerator.generate();
  }

  public void horizontalGreedyRegisterEdges() {
    EntityStructure moveLeftEntityStructure = entityStructureFactory.createEntityStructure();
    moveLeftEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(-1, 0), EmptyBlock.class);
    moveLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, -1), SolidBlock.class);
    //    moveLeftEntityStructure.registerRelativeBlock(new RelativeCoordinates(-1, -1),
    // SolidBlock.class);
    RelativeVertex moveLeftFrom =
        new RelativeVertex(
            moveLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveLeftTo =
        new RelativeVertex(
            moveLeftEntityStructure, new RelativeCoordinates(-1, 0), new Vector2(0, 0));
    HorizontalGreedyEdge moveLeftHorizontalGreedyEdge =
        new HorizontalGreedyEdge(moveLeftEntityStructure, moveLeftFrom, moveLeftTo);

    EntityStructure moveRightEntityStructure = entityStructureFactory.createEntityStructure();
    moveRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(1, 0), EmptyBlock.class);
    moveRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, -1), SolidBlock.class);
    //    moveRightEntityStructure.registerRelativeBlock(new RelativeCoordinates(1, -1),
    // SolidBlock.class);
    RelativeVertex moveRightFrom =
        new RelativeVertex(
            moveRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveRightTo =
        new RelativeVertex(
            moveRightEntityStructure, new RelativeCoordinates(1, 0), new Vector2(0, 0));
    HorizontalGreedyEdge moveRightHorizontalGreedyEdge =
        new HorizontalGreedyEdge(moveRightEntityStructure, moveRightFrom, moveRightTo);

    EntityStructure moveCenterEntityStructure = entityStructureFactory.createEntityStructure();
    moveCenterEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveCenterEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, -1), SolidBlock.class);
    RelativeVertex moveCenterFrom =
        new RelativeVertex(
            moveCenterEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveCenterTo =
        new RelativeVertex(
            moveCenterEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    HorizontalGreedyEdge centerHorizontalGreedyEdge =
        new HorizontalGreedyEdge(moveCenterEntityStructure, moveCenterFrom, moveCenterTo);

    EntityStructure moveDownEntityStructure = entityStructureFactory.createEntityStructure();
    moveDownEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveDownEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, -1), EmptyBlock.class);
    RelativeVertex moveDownFrom =
        new RelativeVertex(
            moveDownEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveDownTo =
        new RelativeVertex(
            moveDownEntityStructure, new RelativeCoordinates(0, -1), new Vector2(0, 0));
    HorizontalGreedyEdge moveDownHorizontalGreedyEdge =
        new HorizontalGreedyEdge(moveDownEntityStructure, moveDownFrom, moveDownTo);

    this.edgeStore.add(moveRightHorizontalGreedyEdge);
    this.edgeStore.add(moveLeftHorizontalGreedyEdge);
    this.edgeStore.add(centerHorizontalGreedyEdge);
    this.edgeStore.add(moveDownHorizontalGreedyEdge);
  }

  public void ladderGreedyRegisterEdges() {

    EntityStructure startLadderEntityStructure = entityStructureFactory.createEntityStructure();
    startLadderEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    RelativeVertex startLadderFrom =
        new RelativeVertex(
            startLadderEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex startLadderTo =
        new RelativeVertex(
            startLadderEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    LadderGreedyEdge startLadderGreedyEdge =
        new LadderGreedyEdge(
            gameController, startLadderEntityStructure, startLadderFrom, startLadderTo);
    this.edgeStore.add(startLadderGreedyEdge);

    EntityStructure climbLadderEntityStructure = entityStructureFactory.createEntityStructure();
    climbLadderEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), Ladder.class);
    climbLadderEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 1), EmptyBlock.class);
    RelativeVertex climbLadderFrom =
        new RelativeVertex(
            climbLadderEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex climbLadderTo =
        new RelativeVertex(
            climbLadderEntityStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0));
    LadderGreedyEdge climbLadderGreedyEdge =
        new LadderGreedyEdge(
            gameController, climbLadderEntityStructure, climbLadderFrom, climbLadderTo);
    this.edgeStore.add(climbLadderGreedyEdge);
  }
}

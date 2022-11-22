package core.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.GameStore;
import core.entity.block.BlockFactory;
import core.entity.block.EmptyBlock;
import core.entity.block.SolidBlock;
import core.entity.misc.Ladder;
import core.entity.pathfinding.edge.DigGreedyEdge;
import core.entity.pathfinding.edge.HorizontalGreedyEdge;
import core.entity.pathfinding.edge.LadderGreedyEdge;

public class EdgeRegistration extends EdgeRegistrationBase {

  @Inject EdgeStore edgeStore;
  @Inject EntityStructureFactory entityStructureFactory;
  @Inject TemplateEdgeGeneratorFactory templateEdgeGeneratorFactory;
  @Inject GameController gameController;
  @Inject GameStore gameStore;
  @Inject BlockFactory blockFactory;

  @Inject
  EdgeRegistration() {}

  public void edgeRegistration() throws BodyNotFound {
    this.templateEdgeRegistration();
    this.horizontalGreedyRegisterEdges();
    this.ladderGreedyRegisterEdges();
    this.digGreedyRegisterEdges();
  }

  @Override
  public void templateEdgeRegistration() throws BodyNotFound {

    TemplateEdgeGenerator templateEdgeGenerator =
        templateEdgeGeneratorFactory.create(
            new RelativeVertex(
                entityStructureFactory.createEntityStructure(),
                new RelativeCoordinates(0, 0),
                new Vector2(0, 0)));
    templateEdgeGenerator.generate();
  }

  @Override
  public void horizontalGreedyRegisterEdges() {
    EntityStructure moveLeftEntityStructure = entityStructureFactory.createEntityStructure();
    moveLeftEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(-1, 0), EmptyBlock.class);
    moveLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, -1), SolidBlock.class);
    RelativeVertex moveLeftFrom =
        new RelativeVertex(
            moveLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveLeftTo =
        new RelativeVertex(
            moveLeftEntityStructure, new RelativeCoordinates(-1, 0), new Vector2(0, 0));
    HorizontalGreedyEdge moveLeftHorizontalGreedyEdge =
        new HorizontalGreedyEdge(
            moveLeftEntityStructure, moveLeftFrom, moveLeftTo, "moveLeftHorizontalGreedyEdge");

    EntityStructure moveRightEntityStructure = entityStructureFactory.createEntityStructure();
    moveRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(1, 0), EmptyBlock.class);
    moveRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, -1), SolidBlock.class);
    RelativeVertex moveRightFrom =
        new RelativeVertex(
            moveRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveRightTo =
        new RelativeVertex(
            moveRightEntityStructure, new RelativeCoordinates(1, 0), new Vector2(0, 0));
    HorizontalGreedyEdge moveRightHorizontalGreedyEdge =
        new HorizontalGreedyEdge(
            moveRightEntityStructure, moveRightFrom, moveRightTo, "moveRightHorizontalGreedyEdge");

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
        new HorizontalGreedyEdge(
            moveCenterEntityStructure, moveCenterFrom, moveCenterTo, "centerHorizontalGreedyEdge");

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
        new HorizontalGreedyEdge(
            moveDownEntityStructure, moveDownFrom, moveDownTo, "moveDownEntityStructure");

    EntityStructure moveLadderRightEntityStructure = entityStructureFactory.createEntityStructure();
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(1, 0), Ladder.class);
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 1), EmptyBlock.class);
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(1, 1), EmptyBlock.class);
    RelativeVertex moveLadderRightFrom =
        new RelativeVertex(
            moveLadderRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveLadderRightTo =
        new RelativeVertex(
            moveLadderRightEntityStructure, new RelativeCoordinates(1, 1), new Vector2(0, 0));
    HorizontalGreedyEdge moveLadderRightHorizontalGreedyEdge =
        new HorizontalGreedyEdge(
            moveLadderRightEntityStructure,
            moveLadderRightFrom,
            moveLadderRightTo,
            "moveLadderRightHorizontalGreedyEdge");

    EntityStructure moveLadderLeftEntityStructure = entityStructureFactory.createEntityStructure();
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(-1, 0), Ladder.class);
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 1), EmptyBlock.class);
    moveLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(-1, 1), EmptyBlock.class);
    RelativeVertex moveLadderLeftFrom =
        new RelativeVertex(
            moveLadderLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveLadderLeftTo =
        new RelativeVertex(
            moveLadderLeftEntityStructure, new RelativeCoordinates(-1, 1), new Vector2(0, 0));
    HorizontalGreedyEdge moveLadderLeftHorizontalGreedyEdge =
        new HorizontalGreedyEdge(
            moveLadderLeftEntityStructure,
            moveLadderLeftFrom,
            moveLadderLeftTo,
            "moveLadderLeftHorizontalGreedyEdge");

    this.edgeStore.add(moveRightHorizontalGreedyEdge);
    this.edgeStore.add(moveLeftHorizontalGreedyEdge);
    this.edgeStore.add(centerHorizontalGreedyEdge);
    this.edgeStore.add(moveDownHorizontalGreedyEdge);
    this.edgeStore.add(moveLadderRightHorizontalGreedyEdge);
    this.edgeStore.add(moveLadderLeftHorizontalGreedyEdge);
  }

  @Override
  public void ladderGreedyRegisterEdges() {
    EntityStructure startLadderLeftEntityStructure = entityStructureFactory.createEntityStructure();
    startLadderLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    //    startLadderLeftEntityStructure.registerRelativeEntity(
    //        new RelativeCoordinates(-1, 0), SolidBlock.class);
    RelativeVertex startLadderFrom =
        new RelativeVertex(
            startLadderLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex startLadderTo =
        new RelativeVertex(
            startLadderLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    LadderGreedyEdge startLadderLeftGreedyEdge =
        new LadderGreedyEdge(
            gameController,
            startLadderLeftEntityStructure,
            startLadderFrom,
            startLadderTo,
            new RelativeCoordinates(-1, 0),
            "startLadderLeftGreedyEdge");

    EntityStructure climbLadderLeftEntityStructure = entityStructureFactory.createEntityStructure();
    climbLadderLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(-1, 0), Ladder.class);
    climbLadderLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    climbLadderLeftEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 1), EmptyBlock.class);
    //    climbLadderLeftEntityStructure.registerRelativeEntity(
    //        new RelativeCoordinates(-1, 1), SolidBlock.class);
    RelativeVertex climbLadderFrom =
        new RelativeVertex(
            climbLadderLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex climbLadderTo =
        new RelativeVertex(
            climbLadderLeftEntityStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0));
    LadderGreedyEdge climbLadderLeftGreedyEdge =
        new LadderGreedyEdge(
            gameController,
            climbLadderLeftEntityStructure,
            climbLadderFrom,
            climbLadderTo,
            new RelativeCoordinates(-1, 1),
            "climbLadderLeftGreedyEdge");

    EntityStructure startLadderRightEntityStructure =
        entityStructureFactory.createEntityStructure();
    startLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    //    startLadderRightEntityStructure.registerRelativeEntity(
    //        new RelativeCoordinates(1, 0), SolidBlock.class);
    RelativeVertex startLadderFromRight =
        new RelativeVertex(
            startLadderRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex startLadderToRight =
        new RelativeVertex(
            startLadderRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    LadderGreedyEdge startLadderRightGreedyEdge =
        new LadderGreedyEdge(
            gameController,
            startLadderRightEntityStructure,
            startLadderFromRight,
            startLadderToRight,
            new RelativeCoordinates(1, 0),
            "startLadderRightGreedyEdge");

    EntityStructure climbLadderRightEntityStructure =
        entityStructureFactory.createEntityStructure();
    climbLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(1, 0), Ladder.class);
    climbLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 0), EmptyBlock.class);
    climbLadderRightEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(0, 1), EmptyBlock.class);
    //    climbLadderRightEntityStructure.registerRelativeEntity(
    //        new RelativeCoordinates(1, 1), SolidBlock.class);
    RelativeVertex climbLadderFromRight =
        new RelativeVertex(
            climbLadderRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex climbLadderToRight =
        new RelativeVertex(
            climbLadderRightEntityStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0));
    LadderGreedyEdge climbLadderRightGreedyEdge =
        new LadderGreedyEdge(
            gameController,
            climbLadderRightEntityStructure,
            climbLadderFromRight,
            climbLadderToRight,
            new RelativeCoordinates(1, 1),
            "climbLadderRightGreedyEdge");

    this.edgeStore.add(startLadderLeftGreedyEdge);
    this.edgeStore.add(climbLadderLeftGreedyEdge);

    this.edgeStore.add(startLadderRightGreedyEdge);
    this.edgeStore.add(climbLadderRightGreedyEdge);
  }

  @Override
  public void digGreedyRegisterEdges() {

    EntityStructure digRightEntityStructure = entityStructureFactory.createEntityStructure();
    digRightEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    digRightEntityStructure.registerRelativeEntity(new RelativeCoordinates(1, 0), SolidBlock.class);
    RelativeVertex digRightPosition =
        new RelativeVertex(
            digRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeCoordinates digPlace = new RelativeCoordinates(1, 0);
    DigGreedyEdge digRightHorizontalGreedyEdge =
        new DigGreedyEdge(
            this.gameController,
            this.gameStore,
            this.blockFactory,
            digRightEntityStructure,
            digRightPosition,
            digPlace,
            "digRightHorizontalGreedyEdge");

    EntityStructure digLeftEntityStructure = entityStructureFactory.createEntityStructure();
    digLeftEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    digLeftEntityStructure.registerRelativeEntity(new RelativeCoordinates(-1, 0), SolidBlock.class);
    RelativeVertex digLeftPosition =
        new RelativeVertex(
            digLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeCoordinates digLeftPlace = new RelativeCoordinates(-1, 0);
    DigGreedyEdge digLeftHorizontalGreedyEdge =
        new DigGreedyEdge(
            this.gameController,
            this.gameStore,
            this.blockFactory,
            digLeftEntityStructure,
            digLeftPosition,
            digLeftPlace,
            "digLeftHorizontalGreedyEdge");

    EntityStructure digDownEntityStructure = entityStructureFactory.createEntityStructure();
    digDownEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    digDownEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, -1), SolidBlock.class);
    RelativeVertex digDownPosition =
        new RelativeVertex(
            digDownEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeCoordinates digDownPlace = new RelativeCoordinates(0, -1);
    DigGreedyEdge digDownHorizontalGreedyEdge =
        new DigGreedyEdge(
            this.gameController,
            this.gameStore,
            this.blockFactory,
            digDownEntityStructure,
            digDownPosition,
            digDownPlace,
            "digDownHorizontalGreedyEdge");

    EntityStructure digUpEntityStructure = entityStructureFactory.createEntityStructure();
    digUpEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    digUpEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 1), SolidBlock.class);
    RelativeVertex digUpPosition =
        new RelativeVertex(digUpEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeCoordinates digUpPlace = new RelativeCoordinates(0, 1);
    DigGreedyEdge digUpHorizontalGreedyEdge =
        new DigGreedyEdge(
            this.gameController,
            this.gameStore,
            this.blockFactory,
            digUpEntityStructure,
            digUpPosition,
            digUpPlace,
            "digUpHorizontalGreedyEdge");

    this.edgeStore.add(digRightHorizontalGreedyEdge);
    this.edgeStore.add(digLeftHorizontalGreedyEdge);
    this.edgeStore.add(digDownHorizontalGreedyEdge);
    this.edgeStore.add(digUpHorizontalGreedyEdge);
  }
}

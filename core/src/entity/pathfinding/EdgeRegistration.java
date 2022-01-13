package entity.pathfinding;

import app.GameController;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import common.GameStore;
import entity.block.BlockFactory;
import entity.block.EmptyBlock;
import entity.block.SolidBlock;
import entity.misc.Ladder;
import entity.pathfinding.edge.DigGreedyEdge;
import entity.pathfinding.edge.HorizontalGreedyEdge;
import entity.pathfinding.edge.LadderGreedyEdge;

public class EdgeRegistration extends EdgeRegistrationBase {

    @Inject
    EdgeStore edgeStore;
    @Inject
    EntityStructureFactory entityStructureFactory;
    @Inject
    TemplateEdgeGeneratorFactory templateEdgeGeneratorFactory;
    @Inject
    GameController gameController;
    @Inject
    GameStore gameStore;
    @Inject
    BlockFactory blockFactory;

    @Inject
    EdgeRegistration() {
    }

    public void edgeRegistration() {
        this.templateEdgeRegistration();
        this.horizontalGreedyRegisterEdges();
        this.ladderGreedyRegisterEdges();
        this.digGreedyRegisterEdges();
    }

    @Override
    public void templateEdgeRegistration() {

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
                new HorizontalGreedyEdge(moveLeftEntityStructure, moveLeftFrom, moveLeftTo);

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

        EntityStructure moveLadderRightEntityStructure = entityStructureFactory.createEntityStructure();
        moveLadderRightEntityStructure.registerRelativeEntity(
                new RelativeCoordinates(0, 0), Ladder.class);
        moveLadderRightEntityStructure.registerRelativeEntity(
                new RelativeCoordinates(1, 0), EmptyBlock.class);
        RelativeVertex moveLadderRightFrom =
                new RelativeVertex(
                        moveLadderRightEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
        RelativeVertex moveLadderRightTo =
                new RelativeVertex(
                        moveLadderRightEntityStructure, new RelativeCoordinates(1, 0), new Vector2(0, 0));
        HorizontalGreedyEdge moveLadderRightHorizontalGreedyEdge =
                new HorizontalGreedyEdge(
                        moveLadderRightEntityStructure, moveLadderRightFrom, moveLadderRightTo);

        EntityStructure moveLadderLeftEntityStructure = entityStructureFactory.createEntityStructure();
        moveLadderLeftEntityStructure.registerRelativeEntity(
                new RelativeCoordinates(0, 0), Ladder.class);
        moveLadderLeftEntityStructure.registerRelativeEntity(
                new RelativeCoordinates(-1, 0), EmptyBlock.class);
        RelativeVertex moveLadderLeftFrom =
                new RelativeVertex(
                        moveLadderLeftEntityStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
        RelativeVertex moveLadderLeftTo =
                new RelativeVertex(
                        moveLadderLeftEntityStructure, new RelativeCoordinates(-1, 0), new Vector2(0, 0));
        HorizontalGreedyEdge moveLadderLeftHorizontalGreedyEdge =
                new HorizontalGreedyEdge(
                        moveLadderLeftEntityStructure, moveLadderLeftFrom, moveLadderLeftTo);

        this.edgeStore.add(moveRightHorizontalGreedyEdge);
        this.edgeStore.add(moveLeftHorizontalGreedyEdge);
        this.edgeStore.add(centerHorizontalGreedyEdge);
        this.edgeStore.add(moveDownHorizontalGreedyEdge);
        this.edgeStore.add(moveLadderRightHorizontalGreedyEdge);
        this.edgeStore.add(moveLadderLeftHorizontalGreedyEdge);
    }

    @Override
    public void ladderGreedyRegisterEdges() {
        EntityStructure startLadderEntityStructure = entityStructureFactory.createEntityStructure();
        startLadderEntityStructure.registerRelativeEntity(
                new RelativeCoordinates(0, 0), EmptyBlock.class);
        startLadderEntityStructure.registerRelativeEntity(
                new RelativeCoordinates(0, -1), SolidBlock.class);
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
                        digPlace);

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
                        digLeftPlace);

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
                        digDownPlace);

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
                        digUpPlace);

        this.edgeStore.add(digRightHorizontalGreedyEdge);
        this.edgeStore.add(digLeftHorizontalGreedyEdge);
        this.edgeStore.add(digDownHorizontalGreedyEdge);
        this.edgeStore.add(digUpHorizontalGreedyEdge);
    }
}

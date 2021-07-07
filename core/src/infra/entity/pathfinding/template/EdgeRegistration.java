package infra.entity.pathfinding.template;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;

public class EdgeRegistration {

  @Inject EdgeStore edgeStore;
  @Inject BlockStructureFactory blockStructureFactory;
  @Inject TemplateEdgeGeneratorFactory templateEdgeGeneratorFactory;

  @Inject
  EdgeRegistration() {}

  public void edgeRegistration() {
    this.greedyRegisterEdges();
        this.templateEdgeRegistration();
  }

  public void templateEdgeRegistration() {

    TemplateEdgeGenerator templateEdgeGenerator =
        templateEdgeGeneratorFactory.create(
            new RelativeVertex(
                blockStructureFactory.createBlockStructure(),
                new RelativeCoordinates(0, 0),
                new Vector2(0, 0)));
    templateEdgeGenerator.applyAction("jump");
    for (int i = 0; i < 100; i++) {
      templateEdgeGenerator.applyAction("right");
    }
  }

  public void greedyRegisterEdges() {
    BlockStructure moveLeftBlockStructure = blockStructureFactory.createBlockStructure();
    moveLeftBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveLeftBlockStructure.registerRelativeBlock(new RelativeCoordinates(-1, 0), EmptyBlock.class);
    moveLeftBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, -1), SolidBlock.class);
    moveLeftBlockStructure.registerRelativeBlock(new RelativeCoordinates(-1, -1), SolidBlock.class);
    RelativeVertex moveLeftFrom =
        new RelativeVertex(
            moveLeftBlockStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveLeftTo =
        new RelativeVertex(
            moveLeftBlockStructure, new RelativeCoordinates(-1, 0), new Vector2(0, 0));
    GreedyEdge moveLeftGreedyEdge =
        new GreedyEdge(moveLeftBlockStructure, moveLeftFrom, moveLeftTo);

    BlockStructure moveRightBlockStructure = blockStructureFactory.createBlockStructure();
    moveRightBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveRightBlockStructure.registerRelativeBlock(new RelativeCoordinates(1, 0), EmptyBlock.class);
    moveRightBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, -1), SolidBlock.class);
    moveRightBlockStructure.registerRelativeBlock(new RelativeCoordinates(1, -1), SolidBlock.class);
    RelativeVertex moveRightFrom =
        new RelativeVertex(
            moveRightBlockStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveRightTo =
        new RelativeVertex(
            moveRightBlockStructure, new RelativeCoordinates(1, 0), new Vector2(0, 0));
    GreedyEdge moveRightGreedyEdge =
        new GreedyEdge(moveRightBlockStructure, moveRightFrom, moveRightTo);

    BlockStructure moveCenterBlockStructure = blockStructureFactory.createBlockStructure();
    moveCenterBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveCenterBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, -1), SolidBlock.class);
    RelativeVertex moveCenterFrom =
        new RelativeVertex(
                moveCenterBlockStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveCenterTo =
        new RelativeVertex(
                moveCenterBlockStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    GreedyEdge centerGreedyEdge =
        new GreedyEdge(moveCenterBlockStructure, moveCenterFrom, moveCenterTo);

    BlockStructure moveDownBlockStructure = blockStructureFactory.createBlockStructure();
    moveDownBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    moveDownBlockStructure.registerRelativeBlock(new RelativeCoordinates(-1, 0), EmptyBlock.class);
    RelativeVertex moveDownFrom =
            new RelativeVertex(
                    moveDownBlockStructure, new RelativeCoordinates(0, 0), new Vector2(0, 0));
    RelativeVertex moveDownTo =
            new RelativeVertex(
                    moveDownBlockStructure, new RelativeCoordinates(0, -1), new Vector2(0, 0));
    GreedyEdge moveDownGreedyEdge =
            new GreedyEdge(moveDownBlockStructure, moveDownFrom, moveDownTo);



    this.edgeStore.add(moveRightGreedyEdge);
    this.edgeStore.add(moveLeftGreedyEdge);
    this.edgeStore.add(centerGreedyEdge);
    this.edgeStore.add(moveDownGreedyEdge);
  }
}

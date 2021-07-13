package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.entity.block.SolidBlock;
import infra.entity.pathfinding.template.*;
import org.junit.Test;

public class testRelativeActionEdgeGenerator {
  @Test
  public void testRelativeActionEdgeGenerator() {
    Injector injector = Guice.createInjector(new SoloConfig());

    RelativeActionEdgeGenerator generator = injector.getInstance(RelativeActionEdgeGenerator.class);

    BlockStructureFactory blockStructureFactory = injector.getInstance(BlockStructureFactory.class);

    BlockStructure blockStructure = blockStructureFactory.createBlockStructure();
    blockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), SolidBlock.class);
    blockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), SolidBlock.class);

    RelativeActionEdge leftRelativeActionEdge =
        generator.generateRelativeActionEdge(
            blockStructure,
            new RelativeVertex(blockStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0)),
            "right");
    //
    //    System.out.println(relativeActionEdge.getFrom());
    //    System.out.println(relativeActionEdge.getTo());
    assert leftRelativeActionEdge.getFrom().relativeCoordinates.getRelativeX()
        < leftRelativeActionEdge.getTo().relativeCoordinates.getRelativeX();

    RelativeActionEdge rightRelativeActionEdge =
        generator.generateRelativeActionEdge(
            blockStructure,
            new RelativeVertex(blockStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0)),
            "left");

    //    System.out.println(relativeActionEdge.getFrom());
    //    System.out.println(relativeActionEdge.getTo());

    assert rightRelativeActionEdge.getFrom().relativeCoordinates.getRelativeX()
        > rightRelativeActionEdge.getTo().relativeCoordinates.getRelativeX();

    RelativeActionEdge stopRelativeActionEdge =
        generator.generateRelativeActionEdge(
            blockStructure,
            new RelativeVertex(blockStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0)),
            "stop");

    //    System.out.println(relativeActionEdge.getFrom());
    //    System.out.println(relativeActionEdge.getTo());

    assert stopRelativeActionEdge.getFrom().relativeCoordinates.getRelativeX()
        == stopRelativeActionEdge.getTo().relativeCoordinates.getRelativeX();

    assert !leftRelativeActionEdge.equals(rightRelativeActionEdge);
  }
}
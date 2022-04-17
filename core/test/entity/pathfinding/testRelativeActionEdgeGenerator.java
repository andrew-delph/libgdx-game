package entity.pathfinding;

import chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.StandAloneConfig;
import entity.block.SolidBlock;
import org.junit.Test;

public class testRelativeActionEdgeGenerator {
  @Test
  public void testRelativeActionEdgeGenerator() throws BodyNotFound {
    Injector injector = Guice.createInjector(new StandAloneConfig());

    RelativeActionEdgeGenerator generator = injector.getInstance(RelativeActionEdgeGenerator.class);

    EntityStructureFactory entityStructureFactory =
        injector.getInstance(EntityStructureFactory.class);

    EntityStructure entityStructure = entityStructureFactory.createEntityStructure();
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), SolidBlock.class);
    entityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), SolidBlock.class);

    RelativeActionEdge leftRelativeActionEdge =
        generator.generateRelativeActionEdge(
            entityStructure,
            new RelativeVertex(entityStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0)),
            "right");
    //
    //    System.out.println(relativeActionEdge.getFrom());
    //    System.out.println(relativeActionEdge.getTo());
    assert leftRelativeActionEdge.getFrom().getRelativeCoordinates().getRelativeX()
        < leftRelativeActionEdge.getTo().getRelativeCoordinates().getRelativeX();

    RelativeActionEdge rightRelativeActionEdge =
        generator.generateRelativeActionEdge(
            entityStructure,
            new RelativeVertex(entityStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0)),
            "left");

    //    System.out.println(relativeActionEdge.getFrom());
    //    System.out.println(relativeActionEdge.getTo());

    assert rightRelativeActionEdge.getFrom().getRelativeCoordinates().getRelativeX()
        > rightRelativeActionEdge.getTo().getRelativeCoordinates().getRelativeX();

    RelativeActionEdge stopRelativeActionEdge =
        generator.generateRelativeActionEdge(
            entityStructure,
            new RelativeVertex(entityStructure, new RelativeCoordinates(0, 1), new Vector2(0, 0)),
            "stop");

    //    System.out.println(relativeActionEdge.getFrom());
    //    System.out.println(relativeActionEdge.getTo());

    assert stopRelativeActionEdge.getFrom().getRelativeCoordinates().getRelativeX()
        == stopRelativeActionEdge.getTo().getRelativeCoordinates().getRelativeX();

    assert !leftRelativeActionEdge.equals(rightRelativeActionEdge);
  }
}

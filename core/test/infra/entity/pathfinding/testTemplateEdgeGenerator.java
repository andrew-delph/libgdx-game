package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.entity.block.Block;
import infra.entity.pathfinding.template.*;
import org.junit.Test;

import java.util.Map;

public class testTemplateEdgeGenerator {
  @Test
  public void testTemplateEdgeGenerator() {
    Injector injector = Guice.createInjector(new SoloConfig());

    TemplateEdgeGeneratorFactory templateEdgeGeneratorFactory =
        injector.getInstance(TemplateEdgeGeneratorFactory.class);

    EdgeStore edgeStore = injector.getInstance(EdgeStore.class);

    BlockStructureFactory blockStructureFactory = injector.getInstance(BlockStructureFactory.class);

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

    System.out.println(edgeStore.getEdgeList().size());

    for (AbstractEdge edge : edgeStore.getEdgeList()) {
      System.out.println(edge);
      System.out.println(edge.blockStructure.getRelativeBlockMapEntrySet().size());
      for (Map.Entry<RelativeCoordinates, Class<? extends Block>> entry :
          edge.blockStructure.getRelativeBlockMapEntrySet()) {
        System.out.println(entry.getKey()+" , "+entry.getValue());
      }
      System.out.println();
    }
  }
}

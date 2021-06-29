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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TemplateEdgeGenerator {
  @Inject BlockStructureFactory blockStructureFactory;
  @Inject EntityBodyBuilder entityBodyBuilder;

  World world;
  Body body;

  public void generate() {
    BlockStructure blockStructureBase = this.blockStructureFactory.createBlockStructure();
    blockStructureBase.registerRelativeBlock(new RelativeCoordinates(0, -1), SolidBlock.class);
    blockStructureBase.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    List<TemplateEdge> templateEdgeList = new LinkedList<>();
    TemplateEdge templateEdge = new TemplateEdge(blockStructureBase.copy());
    RelativeVertex relativeVertex =
        new RelativeVertex(blockStructureBase, new RelativeCoordinates(0, 0), new Vector2(0, 0));
//    RelativeActionEdge relativeActionEdge = relativeVertex.generateRelativeActionEdge("jump");
    //    templateEdge.
  }

  public void setupWorld(BlockStructure blockStructure, RelativeVertex relativeVertex) {
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

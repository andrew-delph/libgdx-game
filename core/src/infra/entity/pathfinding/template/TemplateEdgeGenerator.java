package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;

import java.util.LinkedList;
import java.util.List;

public class TemplateEdgeGenerator {
  BlockStructure blockStructure;

  @Inject RelativeActionEdgeGenerator relativeActionEdgeGenerator;
  @Inject BlockStructureFactory blockStructureFactory;

  @Inject EdgeStore edgeStore;

  RelativeVertex rootRelativeVertex;

  List<RelativeActionEdge> relativeActionEdgeList = new LinkedList<>();

  TemplateEdgeGenerator(
      RelativeActionEdgeGenerator relativeActionEdgeGenerator,
      BlockStructureFactory blockStructureFactory,
      EdgeStore edgeStore,
      RelativeVertex rootRelativeVertex) {
    this.relativeActionEdgeGenerator = relativeActionEdgeGenerator;
    this.blockStructureFactory = blockStructureFactory;
    this.edgeStore = edgeStore;
    this.rootRelativeVertex = rootRelativeVertex;
    BlockStructure rootBlockStructure = this.blockStructureFactory.createBlockStructure();
    rootBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, -1), SolidBlock.class);
    rootBlockStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    this.relativeActionEdgeList.add(
        this.relativeActionEdgeGenerator.generateRelativeActionEdge(
            rootBlockStructure, this.rootRelativeVertex, "jump"));
  }

  public void applyAction(String actionKey) {
    RelativeActionEdge lastActionEdge = this.getLastAction();

    RelativeVertex currentRelativeVertex = lastActionEdge.getTo();

    RelativeActionEdge newActionEdge =
        this.relativeActionEdgeGenerator.generateRelativeActionEdge(
            currentRelativeVertex.blockStructure, currentRelativeVertex, actionKey);

    this.relativeActionEdgeList.add(newActionEdge);

    if (!lastActionEdge
        .getFrom()
        .relativeCoordinates
        .equalBase(lastActionEdge.getTo().relativeCoordinates)) {
      // add to the store
      TemplateEdge newTemplateEdge =
          new TemplateEdge(
              newActionEdge.getTo().blockStructure,
              this.rootRelativeVertex,
              currentRelativeVertex,
              new LinkedList<>(this.relativeActionEdgeList));
      this.edgeStore.add(newTemplateEdge);
    }
  }

  RelativeActionEdge getLastAction() {
    return this.relativeActionEdgeList.get(this.relativeActionEdgeList.size() - 1);
  }
}

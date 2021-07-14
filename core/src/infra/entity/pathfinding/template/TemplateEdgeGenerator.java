package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.entity.block.EmptyBlock;
import infra.entity.block.SolidBlock;

import java.util.*;

public class TemplateEdgeGenerator {
  EntityStructure entityStructure;

  @Inject RelativeActionEdgeGenerator relativeActionEdgeGenerator;
  @Inject EntityStructureFactory entityStructureFactory;

  @Inject EdgeStore edgeStore;

  RelativeVertex rootRelativeVertex;

  List<TemplateEdge> leafTemplateEdge = new LinkedList<>();

  TemplateEdgeGenerator(
      RelativeActionEdgeGenerator relativeActionEdgeGenerator,
      EntityStructureFactory entityStructureFactory,
      EdgeStore edgeStore,
      RelativeVertex rootRelativeVertex) {
    this.relativeActionEdgeGenerator = relativeActionEdgeGenerator;
    this.entityStructureFactory = entityStructureFactory;
    this.edgeStore = edgeStore;
    this.rootRelativeVertex = rootRelativeVertex;
  }

  public void generate() {
    this.generateTree("right");
    this.generateTree("left");
  }

  public void generateTrunk() {

    // create root
    EntityStructure rootEntityStructure = this.entityStructureFactory.createBlockStructure();
    rootEntityStructure.registerRelativeBlock(new RelativeCoordinates(0, -1), SolidBlock.class);
    rootEntityStructure.registerRelativeBlock(new RelativeCoordinates(0, 0), EmptyBlock.class);
    RelativeActionEdge jumpActionEdge =
        this.relativeActionEdgeGenerator.generateRelativeActionEdge(
            rootEntityStructure, this.rootRelativeVertex, "jump");
    TemplateEdge rootTemplateEdge =
        new TemplateEdge(
            rootEntityStructure,
            jumpActionEdge.getFrom(),
            jumpActionEdge.getTo(),
            Collections.singletonList(jumpActionEdge));
    this.leafTemplateEdge.add(rootTemplateEdge);
    TemplateEdge current = this.leafTemplateEdge.get(this.leafTemplateEdge.size() - 1);

    while (true) {

      current = this.applyAction(current, "stop");

      if (current.getLastEdge().from.relativeCoordinates.getRelativeY()
          > current.getLastEdge().to.relativeCoordinates.getRelativeY()) {
        break;
      } else if (!current
          .getLastEdge()
          .from
          .relativeCoordinates
          .equalBase(current.getLastEdge().to.relativeCoordinates)) {
        this.leafTemplateEdge.add(current);
      }
    }
  }

  public void generateTree(String actionKey) {
    this.generateTrunk();

    for (int i = 0; i < 100; i++) {
      //      System.out.println(i + " , " + this.leafTemplateEdge.size());

      Set<TemplateEdge> newLeaves = new HashSet<>();
      while (this.leafTemplateEdge.size() > 0) {
        TemplateEdge current = this.leafTemplateEdge.remove(0);
        newLeaves.add(this.applyAction(current, actionKey));
      }
      this.leafTemplateEdge.addAll(newLeaves);
    }
  }

  public TemplateEdge applyAction(TemplateEdge last, String actionKey) {
    RelativeActionEdge lastActionEdge = last.getLastEdge();

    RelativeVertex currentRelativeVertex = lastActionEdge.getTo();

    RelativeActionEdge newActionEdge =
        this.relativeActionEdgeGenerator.generateRelativeActionEdge(
            currentRelativeVertex.entityStructure.copy(), currentRelativeVertex, actionKey);

    List<RelativeActionEdge> newActionEdgeList = new LinkedList<>(last.getActionEdgeList());
    newActionEdgeList.add(newActionEdge);

    TemplateEdge newTemplateEdge =
        new TemplateEdge(
            newActionEdge.getTo().entityStructure,
            this.rootRelativeVertex,
            newActionEdge.to,
            newActionEdgeList);

    if (!lastActionEdge
        .getFrom()
        .relativeCoordinates
        .equalBase(lastActionEdge.getTo().relativeCoordinates)) {
      // add to the store

      this.edgeStore.add(newTemplateEdge);
    }
    return newTemplateEdge;
  }
}

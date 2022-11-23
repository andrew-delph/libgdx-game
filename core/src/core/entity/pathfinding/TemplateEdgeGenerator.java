package core.entity.pathfinding;

import com.google.inject.Inject;
import core.chunk.world.exceptions.BodyNotFound;
import core.entity.block.EmptyBlock;
import core.entity.block.SolidBlock;
import core.entity.pathfinding.edge.TemplateEdge;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

  public void generate() throws BodyNotFound {
    this.generateTree("right");
    this.generateTree("left");
  }

  public void generateTrunk() throws BodyNotFound {

    // create root
    EntityStructure rootEntityStructure = this.entityStructureFactory.createEntityStructure();
    rootEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, -1), SolidBlock.class);
    rootEntityStructure.registerRelativeEntity(new RelativeCoordinates(0, 0), EmptyBlock.class);
    RelativeActionEdge jumpActionEdge =
        this.relativeActionEdgeGenerator.generateRelativeActionEdge(
            rootEntityStructure, this.rootRelativeVertex, "jump");
    TemplateEdge rootTemplateEdge =
        new TemplateEdge(
            rootEntityStructure,
            jumpActionEdge.getFrom(),
            jumpActionEdge.getTo(),
            Collections.singletonList(jumpActionEdge),
            "root");
    this.leafTemplateEdge.add(rootTemplateEdge);
    TemplateEdge current = this.leafTemplateEdge.get(this.leafTemplateEdge.size() - 1);

    while (true) {

      current = this.applyAction(current, "stop");

      if (current.getLastEdge().from.getRelativeCoordinates().getRelativeY()
          > current.getLastEdge().to.getRelativeCoordinates().getRelativeY()) {
        break;
      } else if (!current
          .getLastEdge()
          .from
          .getRelativeCoordinates()
          .equalBase(current.getLastEdge().to.getRelativeCoordinates())) {
        this.leafTemplateEdge.add(current);
      }
    }
  }

  public void generateTree(String actionKey) throws BodyNotFound {
    this.generateTrunk();

    for (int i = 0; i < 100; i++) {

      Set<TemplateEdge> newLeaves = new HashSet<>();
      while (this.leafTemplateEdge.size() > 0) {
        TemplateEdge current = this.leafTemplateEdge.remove(0);
        newLeaves.add(this.applyAction(current, actionKey));
      }
      this.leafTemplateEdge.addAll(newLeaves);
    }
  }

  public TemplateEdge applyAction(TemplateEdge last, String actionKey) throws BodyNotFound {
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
            newActionEdgeList,
            "edge");

    if (!lastActionEdge
        .getFrom()
        .getRelativeCoordinates()
        .equalBase(lastActionEdge.getTo().getRelativeCoordinates())) {
      // add to the store

      this.edgeStore.add(newTemplateEdge);
    }
    return newTemplateEdge;
  }
}

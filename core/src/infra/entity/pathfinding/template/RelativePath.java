package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.common.Coordinates;

import java.util.*;

public class RelativePath {

  public Coordinates target;
  public RelativePathNode finalPathNode = null;
  Coordinates source;
  @Inject EdgeStore edgeStore;
  Set<RelativePathNode> unvisitedPathNodeSet = new HashSet<>();
  Set<RelativePathNode> visitedPathNodeSet = new HashSet<>();

  public RelativePath(EdgeStore edgeStore, Coordinates source, Coordinates target) {
    this.source = source;
    this.target = target;
    this.edgeStore = edgeStore;
  }

  public void search() throws Exception {
    for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
      if (edge.isAvailable(source)) {
        unvisitedPathNodeSet.add(new RelativePathNode(edge, source, target));
        System.out.println("edge worked");
      }
    }
    while (unvisitedPathNodeSet.size() > 0) {
      RelativePathNode current =
          unvisitedPathNodeSet.stream()
              .min(Comparator.comparingDouble(RelativePathNode::getHeuristic))
              .get();

      if (current.getHeuristic() < 0.9) {
        System.out.println("found " + current.getHeuristic());
        finalPathNode = current;
        return;
      }

      this.unvisitedPathNodeSet.remove(current);
      this.visitedPathNodeSet.add(current);

      for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
        if (edge.isAvailable(current.getEndPosition())) {
          RelativePathNode newNode = new RelativePathNode(edge, current.getEndPosition(), target);

          if (this.visitedPathNodeSet.contains(newNode)
              || this.unvisitedPathNodeSet.contains(newNode)) continue;

          newNode.setPrevious(current);
          unvisitedPathNodeSet.add(newNode);
        }
      }
    }
    throw new Exception("no path found.");
  }

  public List<RelativePathNode> getPathEdgeList() {
    List<RelativePathNode> edgeList = new LinkedList<>();
    RelativePathNode current = finalPathNode;
    while (current != null) {
      edgeList.add(current);
      current = current.getPrevious();
    }
    Collections.reverse(edgeList);
    return edgeList;
  }
}

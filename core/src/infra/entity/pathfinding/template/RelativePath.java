package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.common.Coordinates;

import java.util.*;

public class RelativePath {

  Coordinates source;
  Coordinates target;

  @Inject EdgeStore edgeStore;

  Set<RelativePathNode> unvisitedPathNodeSet = new HashSet<>();
  Set<RelativePathNode> visitedPathNodeSet = new HashSet<>();

  RelativePathNode finalPathNode = null;

  public RelativePath(EdgeStore edgeStore, Coordinates source, Coordinates target) {
    this.source = source;
    this.target = target;
    this.edgeStore = edgeStore;
  }

  public void search() throws Exception {
    for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
      if (edge.isAvailable(source)) {
        unvisitedPathNodeSet.add(new RelativePathNode(edge, source, target));
      }
      else {
        throw new Exception("no edges avail.");
      }
    }
    while (unvisitedPathNodeSet.size() > 0) {
      RelativePathNode current =
              unvisitedPathNodeSet.stream()
                      .min(Comparator.comparingDouble(RelativePathNode::getHeuristic))
                      .get();
      this.unvisitedPathNodeSet.remove(current);
      this.visitedPathNodeSet.add(current);

      for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
        if (edge.isAvailable(current.target)) {
          RelativePathNode newNode = new RelativePathNode(edge, edge.applyTransition(current.currentPosition), target);

          newNode.setPrevious(current);
          unvisitedPathNodeSet.add(newNode);

          if (newNode.getHeuristic() < 0.5) {
            System.out.println("found "+newNode.getHeuristic());
            finalPathNode = newNode;
            return;
          }
        }
      }
    }
    throw new Exception("no path found.");
  }

  public List<RelativePathNode> getPathEdgeList() {
    List<RelativePathNode> edgeList = new LinkedList<>();
    RelativePathNode current = finalPathNode;
    edgeList.add(current);
    while (current.getPrevious() != null) {
      current = current.getPrevious();
      edgeList.add(current);
    }
    return edgeList;
  }
}

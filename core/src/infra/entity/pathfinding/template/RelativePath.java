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
      } else {
        throw new Exception("no edges avail.");
      }
    }
    while (unvisitedPathNodeSet.size() > 0) {
      RelativePathNode current =
          unvisitedPathNodeSet.stream()
              .min(Comparator.comparingDouble(RelativePathNode::getHeuristic))
              .get();
//      System.out.println(
//          "here "
//              + current.currentPosition
//              + " , "
//              + current.edge.applyTransition(current.currentPosition));
//      System.out.println(current.getHeuristic());

      if (current.getHeuristic() < 0.9) {
        System.out.println("found " + current.getHeuristic());
        finalPathNode = current;
        return;
      }

      this.unvisitedPathNodeSet.remove(current);
      this.visitedPathNodeSet.add(current);

      for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
        if (edge.isAvailable(current.getEndPosition())) {
          RelativePathNode newNode =
              new RelativePathNode(edge, current.getEndPosition(), target);

          newNode.setPrevious(current);
//          System.out.println(edge.applyTransition(current.currentPosition).equals(newNode.currentPosition)+",,"+edge.applyTransition(current.currentPosition)+" , "+newNode.currentPosition);
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

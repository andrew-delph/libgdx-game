package infra.entity.pathfinding;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.*;

public class Path {

  Vertex source;
  Vertex target;

  Set<PathNode> unvisitedPathNodeSet = new HashSet<>();
  Set<PathNode> visitedPathNodeSet = new HashSet<>();

  PathNode finalPathNode = null;

  @Inject Graph graph;

  @Inject
  public Path(@Assisted("source") Vertex source, @Assisted("target") Vertex target) {
    this.source = source;
    this.target = target;
    this.unvisitedPathNodeSet.add(
        new PathNode(new ActionEdge(this.source, this.source, null), this.target));
  }

  public void search() {
    while (unvisitedPathNodeSet.size() > 0) {
      PathNode current =
          unvisitedPathNodeSet.stream()
              .min(Comparator.comparingDouble(PathNode::getHeuristic))
              .get();
      //      if (current.edge.from.position.getYReal() >= 1.5) {
      //        System.out.println(
      //            current.edge.actionKey
      //                + " "
      //                + current.edge.from.position
      //                + " "
      //                + current.edge.to.position);
      //      }

      //      if (current.edge.actionKey != null && current.edge.actionKey.equals("jump")) {
      //        System.out.println(current.edge.actionKey + " ; " + current.edge.to.velocity);
      //      }
      //      System.out.println(current.edge.to.position);
      //      if (current.edge.from.velocity.y > 0) {
      //        System.out.println(">>>>> " + current.edge.to.velocity);
      //      } else {
      //        System.out.println(current.edge.from.position + " ; " +
      // current.edge.from.velocity.y);
      //      }

      System.out.println(
          current.actionEdge.actionKey
              + " ; "
              + current.actionEdge.to.position
              + " ; "
              + current.getHeuristic());
      unvisitedPathNodeSet.remove(current);
      visitedPathNodeSet.add(current);
      if (!current.actionEdge.to.isExplored()) {
        current.actionEdge.to.exploreEdges();
      }
      for (ActionEdge actionEdge : this.graph.getEdges(current.actionEdge.to)) {
        //        System.out.println("unvisted "+unvisitedPathNodeSet.size());
        //        System.out.println("visted " + visitedPathNodeSet.size());
        PathNode discoveredPathNode = new PathNode(actionEdge, this.target);

        discoveredPathNode.setPrevious(current);

        if (discoveredPathNode.getHeuristic() < 0.5) {
          System.out.println("found");
          finalPathNode = discoveredPathNode;
          return;
        }

        if (visitedPathNodeSet.contains(discoveredPathNode)
            || unvisitedPathNodeSet.contains(discoveredPathNode)) continue;
        unvisitedPathNodeSet.add(discoveredPathNode);
      }
    }
  }

  public List<ActionEdge> getPathEdgeList() {
    List<ActionEdge> actionEdgeList = new LinkedList<>();
    PathNode current = finalPathNode;
    while (current.getPrevious() != null) {
      current = current.getPrevious();
      actionEdgeList.add(current.actionEdge);
    }
    return actionEdgeList;
  }
}

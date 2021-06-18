package infra.entity.pathfinding;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.*;

public class Path {

  Vertex source;
  Vertex target;

  List<Vertex> visted = new LinkedList<>();

  Set<PathNode> unvisitedPathNodeSet = new HashSet<>();
  Set<PathNode> visitedPathNodeSet = new HashSet<>();

  PathNode finalPathNode = null;

  @Inject Graph graph;

  @Inject
  public Path(@Assisted("source") Vertex source, @Assisted("target") Vertex target) {
    this.source = source;
    this.target = target;
    this.unvisitedPathNodeSet.add(
        new PathNode(new Edge(this.source, this.source, null), this.target));
  }

  public void search() {
    while (unvisitedPathNodeSet.size() > 0) {
      PathNode current =
          unvisitedPathNodeSet.stream()
              .min(Comparator.comparingDouble(PathNode::getHeuristic))
              .get();
      //      System.out.println(current.edge.from.position);
      unvisitedPathNodeSet.remove(current);
      visitedPathNodeSet.add(current);
      //      System.out.println(vistedPathNodeSet.size() + ", " + unvisitedPathNodeSet.size());
      //      System.out.println(current.edge.from.position);
      if (!current.edge.to.isExplored()) {
        current.edge.to.exploreEdges();
      }
      for (Edge edge : this.graph.getEdges(current.edge.to)) {
        PathNode discoveredPathNode = new PathNode(edge, this.target);

        discoveredPathNode.setPrevious(current);

        if (discoveredPathNode.getHeuristic() < 0.5) {
          System.out.println("found the target");
          finalPathNode = discoveredPathNode;
          return;
        }

        if (visitedPathNodeSet.contains(discoveredPathNode)
            || unvisitedPathNodeSet.contains(discoveredPathNode)) continue;
        unvisitedPathNodeSet.add(discoveredPathNode);
      }
    }
  }

  public List<Edge> getPathEdgeList() {
    List<Edge> edgeList = new LinkedList<>();
    PathNode current = finalPathNode;
    while (current.getPrevious() != null) {
      edgeList.add(current.getPrevious().edge);
    }
    return edgeList;
  }
}

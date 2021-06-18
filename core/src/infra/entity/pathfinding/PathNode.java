package infra.entity.pathfinding;

import infra.common.Coordinates;

public class PathNode {

  public Edge edge;
  int heuristic = Integer.MAX_VALUE;
  PathNode previous;
  Vertex target;

  PathNode(Edge edge, Vertex target) {
    this.edge = edge;
    this.target = target;
  }

  public PathNode getPrevious() {
    return previous;
  }

  public void setPrevious(PathNode previous) {
    this.previous = previous;
  }

  public double getHeuristic() {
    Coordinates a = edge.to.position;
    Coordinates b = this.target.position;

    double distance =
        Math.sqrt(
            Math.pow(a.getXReal() - b.getXReal(), 2) + Math.pow(a.getYReal() - b.getYReal(), 2));

    return distance;
  }
}

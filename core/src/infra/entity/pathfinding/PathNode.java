package infra.entity.pathfinding;

public class PathNode {

  int heuristic = Integer.MAX_VALUE;

  PathNode previous;

  Edge edge;

  PathNode(Edge edge) {
    this.edge = edge;
  }

  public PathNode getPrevious() {
    return previous;
  }

  public void setPrevious(PathNode previous) {
    this.previous = previous;
  }

  public int getHeuristic() {
    // return edge.to distance position
    return heuristic;
  }
}

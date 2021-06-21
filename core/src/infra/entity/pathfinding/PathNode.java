package infra.entity.pathfinding;

import infra.common.Coordinates;

public class PathNode {

  public final Edge edge;
  public final Vertex target;

  int heuristic = Integer.MAX_VALUE;
  PathNode previous;

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

    return Math.sqrt(
        Math.pow(a.getXReal() - b.getXReal(), 2) + Math.pow(a.getYReal() - b.getYReal(), 2));
  }

  @Override
  public int hashCode() {
    return (this.edge.hashCode()+","+this.target.hashCode())
            .hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PathNode other = (PathNode) obj;
    return this.edge.equals(other.edge)
            && this.target.equals(other.target);
  }
}

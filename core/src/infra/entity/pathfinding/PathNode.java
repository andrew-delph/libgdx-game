package infra.entity.pathfinding;

import infra.common.Coordinates;

public class PathNode {

  public final ActionEdge actionEdge;
  public final Vertex target;

  int heuristic = Integer.MAX_VALUE;
  PathNode previous;

  PathNode(ActionEdge actionEdge, Vertex target) {
    this.actionEdge = actionEdge;
    this.target = target;
  }

  public PathNode getPrevious() {
    return previous;
  }

  public void setPrevious(PathNode previous) {
    this.previous = previous;
  }

  public double getHeuristic() {
    Coordinates a = actionEdge.to.position;
    Coordinates b = this.target.position.getMiddle();
    Coordinates up = b.getUp();

    double top_left =
        Math.sqrt(
            Math.pow(a.getXReal() - up.getXReal(), 2) + Math.pow(a.getYReal() - up.getYReal(), 2));
    double top_right =
        Math.sqrt(
            Math.pow(a.getXReal() - up.getRight().getXReal(), 2)
                + Math.pow(a.getYReal() - up.getRight().getYReal(), 2));
    double bottom_left =
        Math.sqrt(
            Math.pow(a.getXReal() - b.getXReal(), 2) + Math.pow(a.getYReal() - b.getYReal(), 2));
    double bottom_right =
        Math.sqrt(
            Math.pow(a.getXReal() - b.getRight().getXReal(), 2)
                + Math.pow(a.getYReal() - b.getRight().getYReal(), 2));

    return Math.min(Math.min(Math.min(top_left, top_right), bottom_left), bottom_right);
  }

  @Override
  public int hashCode() {
    return (this.actionEdge.hashCode() + "," + this.target.hashCode()).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PathNode other = (PathNode) obj;
    return this.actionEdge.equals(other.actionEdge) && this.target.equals(other.target);
  }
}

package infra.entity.pathfinding.template;

import infra.common.Coordinates;

public class RelativePathNode {

  public final AbstractEdge edge;
  public final Coordinates currentPosition;
  public final Coordinates target;

  int heuristic = Integer.MAX_VALUE;
  RelativePathNode previous;

  public RelativePathNode(
      AbstractEdge edge, Coordinates currentPosition, Coordinates target) {
    this.currentPosition = currentPosition;
    this.edge = edge;
    this.target = target;
  }

  public RelativePathNode getPrevious() {
    return previous;
  }

  public void setPrevious(RelativePathNode previous) {
    this.previous = previous;
  }

  public double getHeuristic() {
    return Math.sqrt(
        Math.pow(this.target.getXReal() - this.currentPosition.getXReal(), 2)
            + Math.pow(this.target.getYReal() - this.currentPosition.getYReal(), 2));
  }

  @Override
  public String toString() {
    return "RelativePathNode{" +
            "startingPosition=" + currentPosition +
            " ,endingPosition=" + this.edge.applyTransition(currentPosition) +
            ", target=" + target +
            '}';
  }
}

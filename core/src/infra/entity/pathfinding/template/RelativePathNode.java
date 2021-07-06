package infra.entity.pathfinding.template;

import infra.common.Coordinates;

public class RelativePathNode {

  public final AbstractEdge edge;
  public final Coordinates currentPosition;
  public final Coordinates target;

  int heuristic = Integer.MAX_VALUE;
  RelativePathNode previous;

  public RelativePathNode(AbstractEdge edge, Coordinates currentPosition, Coordinates target) {
    this.currentPosition = currentPosition;
    this.edge = edge;
    this.target = target;
  }

  public RelativePathNode getPrevious() {
    return previous;
  }

  public void setPrevious(RelativePathNode previous) {
    System.out.println(">>>>>>"+previous.getEndPosition().equals(this.currentPosition));
    this.previous = previous;
  }

  public Coordinates getEndPosition(){
    return this.edge.applyTransition(this.currentPosition);
  }

  public double getHeuristic() {
    return Math.sqrt(
        Math.pow(
                this.target.getXReal() - this.edge.applyTransition(this.getEndPosition()).getXReal(),
                2)
            + Math.pow(
                this.target.getYReal() - this.edge.applyTransition(this.getEndPosition()).getYReal(),
                2));
  }

  @Override
  public String toString() {
    return "RelativePathNode{"
        + "startingPosition="
        + currentPosition
        + " ,endingPosition="
        + this.edge.applyTransition(currentPosition)
        + ", target="
        + target
        + '}';
  }
}

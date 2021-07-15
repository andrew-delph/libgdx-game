package infra.entity.pathfinding;

import infra.common.Coordinates;
import infra.entity.pathfinding.edge.AbstractEdge;

public class RelativePathNode {

  public final AbstractEdge edge;
  public final Coordinates startPosition;
  public final Coordinates target;
  public PathGameStoreOverride pathGameStoreOverride;

  int heuristic = Integer.MAX_VALUE;
  RelativePathNode previous;

  public RelativePathNode(
      AbstractEdge edge,
      Coordinates startPosition,
      Coordinates target,
      PathGameStoreOverride pathGameStoreOverride) {
    this.startPosition = startPosition;
    this.edge = edge;
    this.target = target;
    this.pathGameStoreOverride = new PathGameStoreOverride(pathGameStoreOverride);
    this.edge.appendPathGameStoreOverride(this.pathGameStoreOverride, this.startPosition);
  }

  public RelativePathNode getPrevious() {
    return previous;
  }

  public void setPrevious(RelativePathNode previous) {
    this.previous = previous;
  }

  public Coordinates getEndPosition() {
    return this.edge.applyTransition(this.startPosition);
  }

  public double getHeuristic() {
    return this.target.calcDistance(this.getEndPosition());
  }

  public void start() {
    System.out.println("start= " + this);
    this.edge.start();
  }

  public boolean finished() {
    return this.edge.isFinished();
  }

  @Override
  public String toString() {
    return "RelativePathNode{"
        + "startingPosition="
        + startPosition
        + " ,endingPosition="
        + this.edge.applyTransition(startPosition)
        + ", target="
        + target
        + '}';
  }

  @Override
  public int hashCode() {
    return (this.startPosition.hashCode() + "," + this.edge.hashCode()).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    RelativePathNode other = (RelativePathNode) obj;
    return this.startPosition.equals(other.startPosition) && this.edge.equals(other.edge);
  }
}

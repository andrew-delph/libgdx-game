package core.entity.pathfinding;

import core.common.Coordinates;
import core.entity.pathfinding.edge.AbstractEdge;

public class RelativePathNode {

  public final AbstractEdge edge;
  public final Coordinates startPosition;
  public final Coordinates target;
  public PathGameStoreOverride pathGameStoreOverride;

  double cost;
  RelativePathNode previous;

  public RelativePathNode(
      AbstractEdge edge,
      Coordinates startPosition,
      Coordinates target,
      PathGameStoreOverride pathGameStoreOverride,
      double cost) {
    this.startPosition = startPosition;
    this.edge = edge;
    this.target = target;
    this.pathGameStoreOverride = new PathGameStoreOverride(pathGameStoreOverride);
    this.edge.appendPathGameStoreOverride(this.pathGameStoreOverride, this.startPosition);
    this.cost = cost;
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

  public double getHeuristicCost() {
    return this.target.calcDistance(this.getEndPosition());
  }

  public double getCostFromStart() {
    return this.cost;
  }

  public double getCost() {
    return this.getHeuristicCost() + this.getCostFromStart();
  }

  public void start() {
    this.edge.start();
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
    return this.startPosition.equals(other.startPosition)
        && this.edge.equals(other.edge)
        && this.edge.getClass() == other.edge.getClass();
  }

  public void render() {
    this.edge.render(startPosition);
  }
}

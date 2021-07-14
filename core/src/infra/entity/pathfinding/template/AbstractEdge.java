package infra.entity.pathfinding.template;

import infra.common.Coordinates;
import infra.entity.Entity;

public abstract class AbstractEdge {

  public EntityStructure entityStructure;
  RelativeVertex from;
  RelativeVertex to;
  boolean finished = false;

  public AbstractEdge(EntityStructure entityStructure, RelativeVertex from, RelativeVertex to) {
    this.entityStructure = entityStructure;
    this.from = from;
    this.to = to;
  }

  public RelativeVertex getFrom() {
    return from;
  }

  public RelativeVertex getTo() {
    return to;
  }

  public abstract void follow(Entity entity, RelativePathNode relativePathNode);

  public boolean isAvailable(Coordinates coordinates) {
    try {
      return this.entityStructure.verifyBlockStructure(coordinates);
    } catch (Exception e) {
      return false;
    }
  }

  public Coordinates applyTransition(Coordinates sourceCoordinates) {
    return this.to.relativeCoordinates.applyRelativeCoordinates(sourceCoordinates);
  }

  public void start() {
    this.finished = false;
  }

  public void finish() {
    this.finished = true;
  }

  public Boolean isFinished() {
    return finished;
  }

  @Override
  public String toString() {
    return this.getClass() + "{" + "from=" + from + ", to=" + to + '}';
  }

  @Override
  public int hashCode() {
    return (this.to.hashCode() + "," + this.from.hashCode()).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    AbstractEdge other = (AbstractEdge) obj;
    return this.to.equals(other.to) && this.from.equals(other.from);
  }
}

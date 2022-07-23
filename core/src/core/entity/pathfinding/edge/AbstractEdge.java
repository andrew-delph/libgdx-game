package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import core.common.Coordinates;
import core.common.GameSettings;
import core.entity.Entity;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.PathGameStoreOverride;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;

public abstract class AbstractEdge {

  public EntityStructure entityStructure;
  public RelativeVertex from;
  public RelativeVertex to;
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

  public abstract EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode);

  public boolean isAvailable(PathGameStoreOverride pathGameStoreOverride, Coordinates coordinates) {
    try {
      return this.entityStructure.verifyEntityStructure(pathGameStoreOverride, coordinates);
    } catch (Exception e) {
      return false;
    }
  }

  public Coordinates applyTransition(Coordinates sourceCoordinates) {
    return this.to.getRelativeCoordinates().applyRelativeCoordinates(sourceCoordinates);
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
    return this.to.equals(other.to)
        && this.from.equals(other.from)
        && this.entityStructure.equals(other.entityStructure);
  }

  public void appendPathGameStoreOverride(
      PathGameStoreOverride pathGameStoreOverride, Coordinates sourceCoordinates) {}

  public double getCost() {
    return 1;
  }

  public void render(Coordinates position) {
    if (GameSettings.RENDER_DEBUG) {
      pathDebugRender.line(
          position.toPhysicsVector2(), this.applyTransition(position).toPhysicsVector2());
    }
  }
}

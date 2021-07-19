package entity.pathfinding.edge;

import entity.Entity;
import entity.pathfinding.RelativePathNode;

public abstract class EdgeStepper {

  boolean finished = false;

  public abstract void follow(Entity entity, RelativePathNode relativePathNode) throws Exception;

  public void finish() {
    this.finished = true;
  }

  public boolean isFinished() {
    return this.finished;
  }
}

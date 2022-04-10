package entity.pathfinding.edge;

import common.exceptions.BodyNotFound;
import common.exceptions.ChunkNotFound;
import common.exceptions.EdgeStepperException;
import entity.Entity;
import entity.pathfinding.RelativePathNode;

public abstract class EdgeStepper {

  boolean finished = false;

  public abstract void follow(Entity entity, RelativePathNode relativePathNode)
      throws EdgeStepperException, ChunkNotFound, BodyNotFound;

  public void finish() {
    this.finished = true;
  }

  public boolean isFinished() {
    return this.finished;
  }
}

package core.entity.pathfinding.edge;

import core.chunk.world.exceptions.BodyNotFound;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.entity.pathfinding.RelativePathNode;
import core.entity.Entity;

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

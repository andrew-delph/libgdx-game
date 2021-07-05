package infra.entity.pathfinding.template;

import com.badlogic.gdx.physics.box2d.Body;
import infra.entity.Entity;

public abstract class AbstractEdge {

  public BlockStructure blockStructure;
  RelativeVertex from;
  RelativeVertex to;
  boolean finished = false;

  public AbstractEdge(BlockStructure blockStructure, RelativeVertex from, RelativeVertex to) {
    this.blockStructure = blockStructure;
    this.from = from;
    this.to = to;
  }

  public abstract void follow(Body body, Entity entity);

  public void finish() {
    this.finished = true;
  }

  public Boolean isFinished() {
    return finished;
  }

  @Override
  public String toString() {
    return "TemplateEdge{" + "from=" + from + ", to=" + to + '}';
  }
}

package infra.entity.pathfinding.template;

import com.badlogic.gdx.physics.box2d.Body;
import infra.common.Coordinates;
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

  public boolean isAvailable(Body body){
    Coordinates bodyCoordinates = new Coordinates(body.getPosition());
    return this.blockStructure.verifyBlockStructure(bodyCoordinates);
  }

  public boolean isAvailable(Coordinates coordinates){
    return this.blockStructure.verifyBlockStructure(coordinates);
  }

  public Coordinates applyTransition(Coordinates sourceCoordinates){
    return this.to.relativeCoordinates.applyRelativeCoordinates(sourceCoordinates);
  }

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

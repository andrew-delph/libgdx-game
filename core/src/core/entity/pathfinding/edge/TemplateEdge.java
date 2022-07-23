package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import com.badlogic.gdx.graphics.Color;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.PathGameStoreOverride;
import core.entity.pathfinding.RelativeActionEdge;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;
import java.util.List;

public class TemplateEdge extends AbstractEdge {

  List<RelativeActionEdge> actionEdgeList;

  int currentStep = 0;

  public TemplateEdge(
      EntityStructure entityStructure,
      RelativeVertex from,
      RelativeVertex to,
      List<RelativeActionEdge> actionEdgeList) {
    super(entityStructure, from, to);
    this.actionEdgeList = actionEdgeList;
  }

  @Override
  public double getCost() {
    return 1;
  }

  public List<RelativeActionEdge> getActionEdgeList() {
    return actionEdgeList;
  }

  @Override
  public boolean isAvailable(PathGameStoreOverride pathGameStoreOverride, Coordinates coordinates) {
    if (!coordinates.equals(coordinates.getBase())) {
      return false;
    }
    return super.isAvailable(pathGameStoreOverride, coordinates);
  }

  @Override
  public void finish() {
    super.finish();
    this.currentStep = 0;
  }

  public void registerActionEdge(RelativeActionEdge actionEdge) {
    this.actionEdgeList.add(actionEdge);
  }

  public RelativeActionEdge getLastEdge() {
    return this.actionEdgeList.get(this.actionEdgeList.size() - 1);
  }

  @Override
  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new TemplateEdgeStepper(this.actionEdgeList);
  }

  @Override
  public void render(Coordinates position) {
    if (GameSettings.RENDER_DEBUG) {
      pathDebugRender.setColor(Color.PURPLE);
      for (RelativeActionEdge actionEdge : this.getActionEdgeList()) {
        Coordinates start =
            actionEdge.getFrom().getRelativeCoordinates().applyRelativeCoordinates(position);
        Coordinates end =
            actionEdge.getTo().getRelativeCoordinates().applyRelativeCoordinates(position);
        pathDebugRender.line(start.toPhysicsVector2(), end.toPhysicsVector2());
      }
    }
  }
}

class TemplateEdgeStepper extends EdgeStepper {

  List<RelativeActionEdge> actionEdgeList;
  int currentStep = 0;

  public TemplateEdgeStepper(List<RelativeActionEdge> actionEdgeList) {
    this.actionEdgeList = actionEdgeList;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode)
      throws BodyNotFound, ChunkNotFound {
    RelativeActionEdge currentEdge = this.actionEdgeList.get(currentStep);
    currentStep++;
    String actionKey = currentEdge.actionKey;
    entity.getEntityController().applyAction(actionKey, entity);
    if (currentStep == this.actionEdgeList.size()) this.finish();
  }
}

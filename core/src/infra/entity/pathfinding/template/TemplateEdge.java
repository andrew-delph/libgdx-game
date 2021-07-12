package infra.entity.pathfinding.template;

import infra.common.Coordinates;
import infra.entity.Entity;

import java.util.List;

public class TemplateEdge extends AbstractEdge {

  List<RelativeActionEdge> actionEdgeList;

  int currentStep = 0;

  public TemplateEdge(
      BlockStructure blockStructure,
      RelativeVertex from,
      RelativeVertex to,
      List<RelativeActionEdge> actionEdgeList) {
    super(blockStructure, from, to);
    this.actionEdgeList = actionEdgeList;
  }

  public List<RelativeActionEdge> getActionEdgeList() {
    return actionEdgeList;
  }

  @Override
  public boolean isAvailable(Coordinates coordinates) {
    if (!coordinates.equals(coordinates.getBase())) {
      return false;
    }
    return super.isAvailable(coordinates);
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
  public void follow(Entity entity, RelativePathNode relativePathNode) {

    RelativeActionEdge currentEdge = this.actionEdgeList.get(currentStep);
    currentStep++;

    String actionKey = currentEdge.actionKey;

    entity.entityController.applyAction(actionKey, entity.getBody());

    //    System.out.println(currentStep+" , "+this.actionEdgeList.size());

    if (currentStep == this.actionEdgeList.size()) this.finish();
  }
}

package infra.entity.pathfinding.template;

import com.badlogic.gdx.physics.box2d.Body;
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
    super(blockStructure, to, from);
    this.actionEdgeList = actionEdgeList;
  }

  public void registerActionEdge(RelativeActionEdge actionEdge) {
    this.actionEdgeList.add(actionEdge);
  }

  public RelativeActionEdge getLastEdge() {
    return this.actionEdgeList.get(this.actionEdgeList.size() - 1);
  }

  @Override
  public void follow(Body body, Entity entity) {
    RelativeActionEdge currentEdge = this.actionEdgeList.get(currentStep);
    currentStep++;

    String actionKey = currentEdge.actionKey;

    entity.entityController.applyAction(actionKey, body);

    if (currentStep == this.actionEdgeList.size()) this.finish();
  }
}

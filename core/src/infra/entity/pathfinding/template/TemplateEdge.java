package infra.entity.pathfinding.template;

import java.util.LinkedList;
import java.util.List;

public class TemplateEdge {
  BlockStructure blockStructure;
  RelativeVertex from;
  RelativeVertex to;

  public TemplateEdge(BlockStructure blockStructure, RelativeVertex from, RelativeVertex to) {
    this.from = from;
    this.to = to;
    this.blockStructure = blockStructure;
  }

  List<RelativeActionEdge> actionEdgeList = new LinkedList<>();

  public void registerActionEdge(RelativeActionEdge actionEdge) {
    this.actionEdgeList.add(actionEdge);
  }

  public RelativeActionEdge getLastEdge() {
    return this.actionEdgeList.get(this.actionEdgeList.size() - 1);
  }
}

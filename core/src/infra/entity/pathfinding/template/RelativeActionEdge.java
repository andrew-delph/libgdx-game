package infra.entity.pathfinding.template;

public class RelativeActionEdge {
  RelativeVertex from;
  RelativeVertex to;
  String actionKey;

  public RelativeActionEdge(RelativeVertex from, RelativeVertex to, String actionKey) {
    this.from = from;
    this.to = to;
    this.actionKey = actionKey;
  }

  public RelativeVertex getFrom() {
    return from;
  }

  public RelativeVertex getTo() {
    return to;
  }
}

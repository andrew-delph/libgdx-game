package entity.pathfinding;

public class RelativeActionEdge {
  public final String actionKey;
  RelativeVertex from;
  RelativeVertex to;

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

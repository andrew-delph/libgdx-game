package infra.entity.pathfinding;

public class Edge {
  public final Vertex from;
  public final Vertex to;
  public final String actionKey;

  Edge(Vertex from, Vertex to, String actionKey) {
    this.from = from;
    this.to = to;
    this.actionKey = actionKey;
  }

  @Override
  public int hashCode() {
    return (this.to.hashCode() + "," + this.from.hashCode() + "," + this.actionKey).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Edge other = (Edge) obj;
    return this.from.equals(other.from)
        && this.to.equals(other.to)
        && this.actionKey.equals(other.actionKey);
  }
}

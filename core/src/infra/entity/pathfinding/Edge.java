package infra.entity.pathfinding;

public class Edge {
  Vertex from;
  Vertex to;
  String actionKey;

  Edge(Vertex from, Vertex to, String actionKey) {
    this.from = from;
    this.to = to;
    this.actionKey = actionKey;
  }
}

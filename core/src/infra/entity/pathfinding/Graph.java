package infra.entity.pathfinding;

import com.google.inject.Inject;

import java.util.*;

public class Graph {
  Map<Vertex, Set<ActionEdge>> graphMap;

  @Inject
  Graph() {
    graphMap = new HashMap<>();
  }

  public void registerVertex(Vertex vertex) {
    if (graphMap.get(vertex) != null) {
      //      System.out.println("already has");
      return;
    } else {
      //      System.out.println("adding " + vertex.hashCode());
    }
    this.graphMap.put(vertex, new HashSet<>());
  }

  public void registerEdge(ActionEdge actionEdge) {
    if (!this.graphMap.containsKey(actionEdge.from)) {
      return;
    }
    this.graphMap.computeIfAbsent(actionEdge.from, k -> new HashSet<>());
    this.graphMap.get(actionEdge.from).add(actionEdge);
  }

  public List<ActionEdge> getEdges(Vertex vertex) {
    return new LinkedList<>(this.graphMap.get(vertex));
  }
}

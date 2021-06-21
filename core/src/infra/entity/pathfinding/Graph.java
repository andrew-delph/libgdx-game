package infra.entity.pathfinding;

import com.google.inject.Inject;

import java.util.*;

public class Graph {
  Map<Vertex, Set<Edge>> graphMap;

  @Inject
  Graph() {
    System.out.println("create graph");
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

  public void registerEdge(Edge edge) {
    if (!this.graphMap.containsKey(edge.from)) {
      return;
    }
    this.graphMap.computeIfAbsent(edge.from, k -> new HashSet<>());
    this.graphMap.get(edge.from).add(edge);
  }

  public List<Edge> getEdges(Vertex vertex) {
    return new LinkedList<>(this.graphMap.get(vertex));
  }
}

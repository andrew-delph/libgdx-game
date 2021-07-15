package infra.entity.pathfinding;

import com.google.inject.Inject;
import infra.entity.pathfinding.edge.AbstractEdge;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EdgeStore {

  Set<AbstractEdge> edges = new HashSet<>();

  @Inject
  EdgeStore() {}

  public void add(AbstractEdge edge) {
    this.edges.add(edge);
  }

  public List<AbstractEdge> getEdgeList() {
    return new LinkedList<>(this.edges);
  }
}

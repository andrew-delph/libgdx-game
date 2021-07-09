package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.entity.Entity;

import java.util.LinkedList;
import java.util.Queue;

public class PathGuider {

  @Inject RelativePathFactory relativePathFactory;

  Entity entity;

  public PathGuider(RelativePathFactory relativePathFactory, Entity entity) {
    this.relativePathFactory = relativePathFactory;
    this.entity = entity;
  }

  RelativePath currentPath;
  Queue<RelativePathNode> pathNodeQueue;
  public RelativePathNode currentPathNode;

  boolean hasPath = false;

  public void findPath(Coordinates start, Coordinates end) throws Exception {
    System.out.println("FIND " + end + " , " + start);
    this.currentPath = relativePathFactory.create(start, end);
    this.currentPath.search();
    this.pathNodeQueue = new LinkedList<>(this.currentPath.getPathEdgeList());
    System.out.println();
    for (RelativePathNode node : this.pathNodeQueue) {
      System.out.println(node);
    }
    System.out.println();
    this.hasPath = true;
  }

  public boolean hasPath() {
    return this.hasPath;
  }

  public void followPath() {
    if (this.currentPathNode == null || this.currentPathNode.finished()) {
      this.currentPathNode = this.pathNodeQueue.poll();
      if (this.currentPathNode == null) {
        this.hasPath = false;
        return;
      } else {
        System.out.println("startPosition = " + this.currentPathNode.startPosition);
        System.out.println("currentPosition = " + this.entity.coordinates);
        System.out.println("target = " + this.currentPathNode.getEndPosition());
        System.out.println("edge = " + this.currentPathNode.edge.getClass());
        System.out.println("vertex pos = " + new Coordinates(this.entity.getBody().getPosition()));
        this.currentPathNode.start();
      }
    }

    this.currentPathNode.edge.follow(this.entity, this.currentPathNode);
  }
}

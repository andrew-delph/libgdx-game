package entity.pathfinding;

import com.google.inject.Inject;
import common.Coordinates;
import entity.Entity;
import entity.pathfinding.edge.EdgeStepper;

import java.util.LinkedList;
import java.util.Queue;

public class PathGuider {

  public RelativePathNode currentPathNode;
  @Inject RelativePathFactory relativePathFactory;
  Entity entity;
  RelativePath currentPath;
  EdgeStepper currentEdgeStepper;
  Queue<RelativePathNode> pathNodeQueue;
  boolean hasPath = false;

  public PathGuider(RelativePathFactory relativePathFactory, Entity entity) {
    this.relativePathFactory = relativePathFactory;
    this.entity = entity;
  }

  public void findPath(Coordinates start, Coordinates end) throws Exception {
    System.out.println("FIND " + start + " , " + end);
    this.currentPath = relativePathFactory.create(start, end);
    this.currentPath.search();
    this.pathNodeQueue = new LinkedList<>(this.currentPath.getPathEdgeList());
    this.hasPath = true;
  }

  public boolean hasPath() {
    return this.hasPath;
  }

  public void followPath() {
    if (this.currentPathNode == null || this.currentEdgeStepper.isFinished()) {
      this.currentPathNode = this.pathNodeQueue.poll();
      if (this.currentPathNode == null) {
        this.hasPath = false;
        return;
      } else {
        this.currentEdgeStepper = currentPathNode.edge.getEdgeStepper(entity, currentPathNode);
        this.entity.getBody().setTransform(this.currentPathNode.startPosition.toVector2(), 0);
        //        this.entity.getBody().setLinearVelocity(this.currentPathNode.edge.from.velocity);
        this.entity.coordinates = this.currentPathNode.startPosition;
        this.currentPathNode.start();
      }
    }
    System.out.println(this.currentPathNode + " , " + this.currentPathNode.edge.getClass());
    try {
      this.currentEdgeStepper.follow(this.entity, this.currentPathNode);
    } catch (Exception e) {
      this.hasPath = false;
    }
  }
}

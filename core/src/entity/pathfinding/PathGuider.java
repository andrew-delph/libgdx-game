package entity.pathfinding;

import chunk.world.exceptions.BodyNotFound;
import com.google.inject.Inject;
import common.exceptions.ChunkNotFound;
import common.exceptions.EdgeStepperException;
import entity.Entity;
import entity.attributes.msc.Coordinates;
import entity.pathfinding.edge.EdgeStepper;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PathGuider {
  final Logger LOGGER = LogManager.getLogger();
  public RelativePathNode currentPathNode;
  @Inject RelativePathFactory relativePathFactory;
  Entity entity;
  RelativePath currentPath;
  EdgeStepper currentEdgeStepper;
  Queue<RelativePathNode> pathNodeQueue;

  public PathGuider(RelativePathFactory relativePathFactory, Entity entity) {
    this.relativePathFactory = relativePathFactory;
    this.entity = entity;
  }

  public void findPath(Coordinates start, Coordinates end) {
    this.pathNodeQueue = null;
    this.currentPath = relativePathFactory.create(start, end);
    this.currentPath.backgroundSearch();
  }

  public void followPath(Coordinates coordinates) throws BodyNotFound, ChunkNotFound {
    if (this.currentPath != null && this.currentPath.isSearching()) {
      return;
    } else if (this.currentPath == null) {
      this.findPath(entity.coordinates, coordinates);
      return;
    }
    if (!this.currentPath.isSearching() && this.pathNodeQueue == null) {
      this.pathNodeQueue = new LinkedList<>(this.currentPath.getPathEdgeList());
    }

    if (this.currentPathNode == null || this.currentEdgeStepper.isFinished()) {
      this.currentPathNode = this.pathNodeQueue.poll();
      if (this.currentPathNode == null) {
        this.findPath(entity.coordinates, coordinates);
        return;
      } else {
        // start using a new path
        this.currentEdgeStepper = currentPathNode.edge.getEdgeStepper(entity, currentPathNode);
        this.entity.setBodyPosition(this.currentPathNode.startPosition.toVector2());
        this.entity.coordinates = this.currentPathNode.startPosition;
        this.currentPathNode.start();
      }
    }

    try {
      this.currentEdgeStepper.follow(this.entity, this.currentPathNode);
    } catch (EdgeStepperException | ChunkNotFound e) {
      LOGGER.debug("Edge stepper error: " + e);
      this.reset();
    }
  }

  public void reset() {
    this.currentPath = null;
    this.currentEdgeStepper = null;
    this.pathNodeQueue = null;
    this.currentPathNode = null;
  }

  public void render() {
    if (this.currentPathNode != null) this.currentPathNode.render();
    if (this.pathNodeQueue == null) return;
    for (RelativePathNode pathNode : this.pathNodeQueue) {
      pathNode.render();
    }
  }
}

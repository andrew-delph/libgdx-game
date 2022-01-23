package entity.pathfinding;

import com.google.inject.Inject;
import common.Coordinates;
import entity.Entity;
import entity.pathfinding.edge.EdgeStepper;

import java.util.LinkedList;
import java.util.Queue;

public class PathGuider {

    public RelativePathNode currentPathNode;
    @Inject
    RelativePathFactory relativePathFactory;
    Entity entity;
    RelativePath currentPath;
    EdgeStepper currentEdgeStepper;
    Queue<RelativePathNode> pathNodeQueue;

    public PathGuider(RelativePathFactory relativePathFactory, Entity entity) {
        this.relativePathFactory = relativePathFactory;
        this.entity = entity;
    }

    public void findPath(Coordinates start, Coordinates end) throws Exception {
        this.pathNodeQueue = null;
        this.currentPath = relativePathFactory.create(start, end);
        this.currentPath.backgroundSearch();
    }

    public void followPath(Coordinates coordinates) throws Exception {
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
                this.entity.getBody().setTransform(this.currentPathNode.startPosition.toVector2(), 0);
                //        this.entity.getBody().setLinearVelocity(this.currentPathNode.edge.from.velocity);
                this.entity.coordinates = this.currentPathNode.startPosition;
                this.currentPathNode.start();
            }
        }

        try {
            this.currentEdgeStepper.follow(this.entity, this.currentPathNode);
            System.out.println("currentEdgeStepper: " + this.currentEdgeStepper);
        } catch (Exception e) {
            e.printStackTrace();
            this.reset();
            return;
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

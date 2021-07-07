package infra.entity.pathfinding.template;

import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.entity.Entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PathGuider {

    @Inject RelativePathFactory relativePathFactory;

    Entity entity;

    public PathGuider(RelativePathFactory relativePathFactory,Entity entity) {
        this.relativePathFactory = relativePathFactory;
        this.entity = entity;
    }

    RelativePath currentPath;
    Queue<RelativePathNode> pathNodeQueue;
    RelativePathNode currentPathNode;

    boolean hasPath = false;

    public void findPath(Coordinates start, Coordinates end) throws Exception {
        this.currentPath = relativePathFactory.create(start,end);
        this.currentPath.search();
        this.pathNodeQueue = new LinkedList<>(this.currentPath.getPathEdgeList());
    }

    public boolean hasPath(){
        return this.hasPath;
    }

    public void followPath(){
        if(this.currentPathNode == null || this.currentPathNode.finished()){
            this.currentPathNode = this.pathNodeQueue.poll();
        }
        if(this.currentPathNode == null) {
            this.hasPath = false;
            return;
        }

        this.currentPathNode.edge.follow(this.entity);
    }
}

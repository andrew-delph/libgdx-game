package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.entity.block.Block;
import infra.entity.controllers.actions.EntityAction;

import java.util.List;
import java.util.Map;

public class Vertex {
  World world;

  @Inject GameStore gameStore;

  @Inject Graph graph;

  @Inject VertexFactory vertexFactory;

  Entity entity;
  Coordinates position;
  Vector2 velocity;
  Body body;
  Boolean explored = false;

  @Inject
  public Vertex(
      @Assisted Entity entity, @Assisted Coordinates position, @Assisted Vector2 velocity) {
    this.entity = entity;
    this.position = position;
    this.velocity = velocity;
  }

  public void setupWorld() {
    this.world = new World(new Vector2(0, -1f), false);

    List<Block> blockList =
        this.gameStore.getBlockInRange(
            this.position.getLeft().getDown(), this.position.getRight().getUp());

    for (Block block : blockList) {
      block.addWorld(this.world);
    }

    this.body = this.entity.addWorld(this.world);
    this.body.setTransform(this.position.toVector2(), 0);
    this.body.setLinearVelocity(this.velocity);
  }

  public Boolean isExplored() {
    return this.explored;
  }

  public void exploreEdges() {
    this.explored = true;
    for (Map.Entry<String, EntityAction> entry :
        this.entity.entityController.getEntityActionEntrySet()) {
      String actionKey = entry.getKey();
//      if (actionKey.equals("stop")) {
//        if(this.velocity.x == 0) continue;
//      };
//      if (actionKey.equals("jump")) {
//        if(this.velocity.y != 0) continue;
////        System.out.println(actionKey);
//      };
//      if (actionKey.equals("left") || actionKey.equals("right")) {
//        if(this.velocity.y > 0) continue;
//      }
//
//      if (!(actionKey.equals("jump")||actionKey.equals("stop"))){
//        continue;
//      }

      EntityAction action = entry.getValue();
      this.setupWorld();
      action.apply(this.body);
      world.step(1 / 5f, 6, 2);

      Vertex newVertex =
          vertexFactory.createVertex(
              this.entity, new Coordinates(this.body.getPosition()), this.body.getLinearVelocity());
      graph.registerVertex(newVertex);
      Edge newEdge = new Edge(this, newVertex, actionKey);

      if (!newEdge.from.position.equals(newEdge.to.position)) graph.registerEdge(newEdge);
      else System.out.println("position no move");

//      world.dispose();
//      Runtime.getRuntime().gc();
    }
  }

  @Override
  public int hashCode() {
    return (this.entity.hashCode()
            + ","
            + this.position.hashCode()
            + ","
            + this.velocity.x
            + ","
            + this.velocity.y)
        .hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Vertex other = (Vertex) obj;
    return this.entity.equals(other.entity)
        && this.position.equals(other.position)
        && this.velocity.x == other.velocity.x
        && this.velocity.y == other.velocity.y;
  }
}

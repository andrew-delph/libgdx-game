package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;
import infra.entity.block.Block;
import infra.entity.controllers.actions.EntityAction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Vertex {
  World world;

  @Inject GameStore gameStore;

  Entity entity;
  Coordinates position;
  Vector2 velocity;
  Body body;

  List<Vertex> children = new LinkedList<>();

  public Vertex(Entity entity, Coordinates position, Vector2 velocity) {
    this.entity = entity;
    this.position = position;
    this.velocity = velocity;
  }

  public void setupWorld() {
    this.world = new World(new Vector2(0, -1), false);

    List<Block> blockList =
        this.gameStore.getBlockInRange(
            this.position.getLeft().getDown(), this.position.getRight().getUp());

    for (Block block : blockList) {
      block.addWorld(this.world);
    }

    this.body = this.entity.addWorld(this.world);
  }

  public void generateEdges() {
    for (Map.Entry<String, EntityAction> entry :
        this.entity.entityController.getEntityActionEntrySet()) {
      String actionKey = entry.getKey();
      if (actionKey.equals("jump")) continue;
      EntityAction action = entry.getValue();
      this.setupWorld();
      action.apply(this.body);
      world.step(1, 6, 2);
      new Edge(
          this,
          new Vertex(
              this.entity, new Coordinates(this.body.getPosition()), this.body.getLinearVelocity()),
          actionKey);
    }
  }
}

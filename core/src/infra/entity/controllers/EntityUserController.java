package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.app.GameController;
import infra.common.Direction;
import infra.entity.Entity;
import infra.entity.block.DirtBlock;
import infra.entity.block.SkyBlock;
import infra.entity.controllers.actions.EntityActionFactory;

public class EntityUserController extends EntityController {

  @Inject GameController gameController;

  @Inject
  public EntityUserController(EntityActionFactory entityActionFactory, @Assisted Entity entity) {
    super(entityActionFactory, entity);
    this.registerAction("left", entityActionFactory.createHorizontalMovementAction(-7));
    this.registerAction("right", entityActionFactory.createHorizontalMovementAction(7));
  }

  @Override
  public void beforeWorldUpdate() {

    Body body = this.entity.getBody();

    float impulse = body.getMass() * 10;
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        this.gameController.placeBlock(this.entity, Direction.LEFT, SkyBlock.class);
      } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        this.gameController.placeBlock(this.entity, Direction.RIGHT, SkyBlock.class);
      } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        this.gameController.placeBlock(this.entity, Direction.DOWN, SkyBlock.class);
      } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        this.gameController.placeBlock(this.entity, Direction.UP, SkyBlock.class);
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        this.gameController.placeBlock(this.entity, Direction.LEFT, DirtBlock.class);
      } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        this.gameController.placeBlock(this.entity, Direction.RIGHT, DirtBlock.class);
      } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        this.gameController.placeBlock(this.entity, Direction.DOWN, DirtBlock.class);
      } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        this.gameController.placeBlock(this.entity, Direction.UP, DirtBlock.class);
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      if (this.getAction("jump").isValid(body)) {
        this.applyAction("jump", body);
      }
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      this.applyAction("stop", body);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      this.applyAction("left", body);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      this.applyAction("right", body);
    }
  }
}

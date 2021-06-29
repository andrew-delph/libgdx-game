package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import infra.app.GameController;
import infra.common.Coordinates;
import infra.common.Direction;
import infra.entity.Entity;
import infra.entity.block.DirtBlock;
import infra.entity.block.SkyBlock;

public class EntityUserController extends EntityController {

  public EntityUserController(GameController gameController, Entity entity) {
    super(gameController, entity);
  }

  @Override
  public void beforeWorldUpdate() {
    this.entity.getBody().setLinearVelocity(-5f, 0f);
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
    } else {
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        this.entity.getBody().setLinearVelocity(-5f, 0f);
      }
      if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        this.entity.getBody().setLinearVelocity(5f, 0f);
      }
      if (Gdx.input.isKeyPressed(Input.Keys.S)) {}
      if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        this.entity.getBody().setLinearVelocity(0f, 50f);
      }
    }
  }

  @Override
  public void afterWorldUpdate() {
    gameController.moveEntity(
        this.entity.uuid,
        new Coordinates(
            this.entity.getBody().getPosition().x / Entity.coordinatesScale,
            this.entity.getBody().getPosition().y / Entity.coordinatesScale));
  }
}

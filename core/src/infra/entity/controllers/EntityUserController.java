package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.app.GameController;
import infra.common.Coordinates;
import infra.common.Direction;
import infra.entity.Entity;

public class EntityUserController extends EntityController {

  @Inject GameController gameController;

  @Inject
  public EntityUserController(@Assisted Entity entity) {
    super(entity);
  }

  @Override
  public void beforeWorldUpdate() {
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        this.gameController.dig(this.entity, Direction.LEFT);
      } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        this.gameController.dig(this.entity, Direction.RIGHT);
      } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        this.gameController.dig(this.entity, Direction.DOWN);
      } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        this.gameController.dig(this.entity, Direction.UP);
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

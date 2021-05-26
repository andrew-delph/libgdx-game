package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.app.GameController;
import infra.common.Coordinates;
import infra.entity.Entity;

public class EntityUserController extends EntityController {

  @Inject GameController gameController;

  @Inject
  public EntityUserController(@Assisted Entity entity) {
    super(entity);
  }

  @Override
  public void update() {
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      gameController.moveEntity(
          this.entity,
          new Coordinates(
              this.entity.coordinates.getXReal() - 0.1f, this.entity.coordinates.getYReal()));
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      gameController.moveEntity(
          this.entity,
          new Coordinates(
              this.entity.coordinates.getXReal() + 0.1f, this.entity.coordinates.getYReal()));
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      gameController.moveEntity(
          this.entity,
          new Coordinates(
              this.entity.coordinates.getXReal(), this.entity.coordinates.getYReal() - 0.1f));
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      gameController.moveEntity(
          this.entity,
          new Coordinates(
              this.entity.coordinates.getXReal(), this.entity.coordinates.getYReal() + 0.1f));
    }
  }
}

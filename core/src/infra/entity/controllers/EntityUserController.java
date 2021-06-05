package infra.entity.controllers;

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
  public void beforeWorldUpdate() {
    //    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
    //      gameController.moveEntity(
    //          this.entity.uuid,
    //          new Coordinates(
    //              this.entity.coordinates.getXReal() - 0.1f, this.entity.coordinates.getYReal()));
    //    }
    //    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
    //      gameController.moveEntity(
    //          this.entity.uuid,
    //          new Coordinates(
    //              this.entity.coordinates.getXReal() + 0.1f, this.entity.coordinates.getYReal()));
    //    }
    //    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
    //      gameController.moveEntity(
    //          this.entity.uuid,
    //          new Coordinates(
    //              this.entity.coordinates.getXReal(), this.entity.coordinates.getYReal() - 0.1f));
    //    }
    //    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
    //      gameController.moveEntity(
    //          this.entity.uuid,
    //          new Coordinates(
    //              this.entity.coordinates.getXReal(), this.entity.coordinates.getYReal() + 0.1f));
    //    }
  }

  @Override
  public void afterWorldUpdate() {
    //    System.out.println("here");

    gameController.moveEntity(
        this.entity.uuid,
        new Coordinates(
            this.entity.body.getPosition().x / Entity.coordinatesScale,
            this.entity.body.getPosition().y / Entity.coordinatesScale));
  }
}

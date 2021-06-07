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
    System.out.println("aaa"+this.entity.getBody());
  }

  @Override
  public void beforeWorldUpdate() {
//    System.out.println("before"+this.entity.getBody());
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      this.entity.getBody().setLinearVelocity(-1f, 0f);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      this.entity.getBody().setLinearVelocity(1f, 0f);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {}

    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {}
  }

  @Override
  public void afterWorldUpdate() {
    //    System.out.println("here" + this.entity.body.getPosition());
    //    System.out.println(
    //        "..."
    //            + new Coordinates(
    //                this.entity.body.getPosition().x / Entity.coordinatesScale,
    //                this.entity.body.getPosition().y / Entity.coordinatesScale));

    gameController.moveEntity(
        this.entity.uuid,
        new Coordinates(
            this.entity.getBody().getPosition().x / Entity.coordinatesScale,
            this.entity.getBody().getPosition().y / Entity.coordinatesScale));
  }
}

package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.app.GameController;
import infra.common.Clock;
import infra.common.Coordinates;
import infra.common.Direction;
import infra.entity.Entity;
import infra.entity.block.DirtBlock;
import infra.entity.block.SkyBlock;

public class EntityUserController extends EntityController {

  @Inject GameController gameController;

  @Inject
  Clock clock;

  int lastJump = 0;

  @Inject
  public EntityUserController(@Assisted Entity entity) {
    super(entity);
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
    } else if (body.getLinearVelocity().y == 0) {
      if (Gdx.input.isKeyPressed(Input.Keys.W) && clock.currentTick.time - lastJump > 5) {
        lastJump = clock.currentTick.time;
        body.setLinearVelocity(new Vector2(0,9));
//        body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x, impulse), body.getWorldCenter(), true);
      }

      if (Gdx.input.isKeyPressed(Input.Keys.S)) {}
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      body.setLinearVelocity(new Vector2(-5,body.getLinearVelocity().y));

    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//      body.applyForceToCenter(new Vector2(impulse, 0), true);
      body.setLinearVelocity(new Vector2(5,body.getLinearVelocity().y));
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

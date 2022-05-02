package entity.controllers;

import app.GameController;
import chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import common.Direction;
import common.events.EventService;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import entity.controllers.actions.EntityActionFactory;
import networking.events.EventTypeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityUserController extends EntityController {

  final Logger LOGGER = LogManager.getLogger();

  public EntityUserController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void beforeWorldUpdate() throws EntityNotFound, ChunkNotFound, BodyNotFound {
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
    if (Gdx.input.isKeyPressed(Input.Keys.E)) {
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        this.gameController.createLadder(this.entity.coordinates.getMiddle().getLeft().getBase());
      } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        this.gameController.createLadder(this.entity.coordinates.getMiddle().getRight().getBase());
      } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        this.gameController.createLadder(this.entity.coordinates.getMiddle().getDown().getBase());
      } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        this.gameController.createLadder(this.entity.coordinates.getMiddle().getUp().getBase());
      }
    }

    if (Gdx.input.isKeyJustPressed(Keys.F1)) {
      gameController.createProjectile(this.entity.coordinates.getMiddle(), new Vector2(10, 0));
    }

    if (Gdx.input.isKeyJustPressed(Keys.F2)) {
      gameController.createTurret(this.entity.coordinates.getBase());
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
      gameController.createAI(this.entity.getUuid());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      if (this.getAction("climbUp").isValid(entity)) {
        this.applyAction("climbUp", entity);
      } else if (this.getAction("jump").isValid(entity)) {
        this.applyAction("jump", entity);
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      if (this.getAction("climbDown").isValid(entity)) {
        this.applyAction("climbDown", entity);
      } else {
        this.applyAction("stop", entity);
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      if (this.getAction("left").isValid(entity)) {
        this.applyAction("left", entity);
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      if (this.getAction("right").isValid(entity)) {
        this.applyAction("right", entity);
      }
    }
  }
}

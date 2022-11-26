package core.entity.attributes.inventory.item.comsumers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Coordinates;
import core.common.Direction;
import core.common.GameSettings;
import core.common.Util;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.Health;
import core.entity.collision.RayCastService;
import java.util.LinkedList;
import java.util.List;

public class DefaultItemAction implements ItemActionInterface {

  @Inject RayCastService rayCastService;
  @Inject GameController gameController;

  @Override
  public void use(Entity controlee) {
    Coordinates myCoordinates = controlee.getCoordinatesWrapper().getCoordinates();

    Coordinates directionCoords;

    if (controlee.getDirectionWrapper().getDirection().equals(Direction.RIGHT)) {
      directionCoords = myCoordinates.add(1, 0);
    } else {
      directionCoords = myCoordinates.add(-1, 0);
    }

    List<Entity> hitEntityList =
        new LinkedList<>(rayCastService.rayCast(myCoordinates, directionCoords));

    for (Entity hitEntity : hitEntityList) {
      if (hitEntity.equals(controlee)) continue;

      if (!hitEntity.getClass().equals(Entity.class)) continue;

      Coordinates hitCoordinates = hitEntity.getCoordinatesWrapper().getCoordinates();

      Health health = hitEntity.getHealth().applyDiff(-52);

      // calculate angle, apply some force...
      // apply a boop in the opposite direction. and up
      Vector2 attackPhysicsVector = Util.calcVelocity(myCoordinates, hitCoordinates, 10);
      attackPhysicsVector.y = 10;

      try {
        hitEntity.setBodyVelocity(attackPhysicsVector);
      } catch (ChunkNotFound | BodyNotFound e) {
        Gdx.app.error(
            GameSettings.LOG_TAG,
            ("Cannot update entity: " + hitEntity.getUuid().toString() + " Velocity"));
      }

      try {
        gameController.updateEntityAttribute(hitEntity.getUuid(), health);
      } catch (EntityNotFound e) {
        Gdx.app.error(
            GameSettings.LOG_TAG,
            ("Cannot update entity: " + hitEntity.getUuid().toString() + " Health"));
      }
    }
  }
}

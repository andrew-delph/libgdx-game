package core.entity.attributes.inventory.item.comsumers;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.Coordinates;
import core.common.Util;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.Health;
import core.entity.collision.RayCastService;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultItemAction implements ItemActionInterface {

  final Logger LOGGER = LogManager.getLogger();
  @Inject RayCastService rayCastService;
  @Inject GameController gameController;

  @Override
  public void use(Entity controlee) {
    Coordinates myCoordinates = controlee.getCoordinatesWrapper().getCoordinates();

    List<Entity> hitEntityList =
        new LinkedList<>(rayCastService.rayCast(myCoordinates.getLeft(), myCoordinates.getRight()));

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
        LOGGER.error("Cannot update entity: " + hitEntity.getUuid().toString() + " Velocity");
      }

      try {
        gameController.updateEntityAttribute(hitEntity.getUuid(), health);
      } catch (EntityNotFound e) {
        LOGGER.error("Cannot update entity: " + hitEntity.getUuid().toString() + " Health");
      }
    }
  }
}

package entity.attributes.inventory.item.comsumers;

import app.game.GameController;
import com.google.inject.Inject;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.attributes.msc.Coordinates;
import entity.attributes.msc.Health;
import entity.collision.RayCastService;
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
    Coordinates center = controlee.getCenter();

    List<Entity> hitEntityList =
        new LinkedList<>(rayCastService.rayCast(center.getLeft(), center.getRight()));

    for (Entity hitEntity : hitEntityList) {
      if (hitEntity.equals(controlee)) continue;

      if (!hitEntity.getClass().equals(Entity.class)) continue;

      Health health = hitEntity.getHealth().applyDiff(-52);
      try {
        gameController.updateEntityAttribute(hitEntity.getUuid(), health);
      } catch (EntityNotFound e) {
        LOGGER.error("Cannot update client: " + hitEntity.getUuid().toString() + " Health");
      }
    }
  }
}

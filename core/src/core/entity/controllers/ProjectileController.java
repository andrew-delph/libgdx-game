package core.entity.controllers;

import core.app.game.GameController;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.Coordinates;
import core.entity.attributes.msc.Health;
import core.entity.block.Block;
import core.entity.collision.projectile.ProjectileContact;
import core.entity.controllers.actions.EntityActionFactory;
import core.networking.events.EventTypeFactory;
import java.util.UUID;

public class ProjectileController extends EntityController {

  Coordinates startPosition;
  ProjectileContact projectileContact;
  GameStore gameStore;
  float distanceRange;

  public ProjectileController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      GameStore gameStore,
      Entity entity,
      ProjectileContact projectileContact,
      Coordinates startPosition,
      float distanceRange) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    this.startPosition = startPosition;
    this.distanceRange = distanceRange;
    this.projectileContact = projectileContact;
    this.gameStore = gameStore;
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    super.beforeWorldUpdate();
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    Coordinates moveTo =
        new Coordinates(
            this.entity.getBodyPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);
    // if distance traveled goes over max. destroy it
    if (moveTo.calcDistance(this.startPosition) > distanceRange) {
      gameController.removeEntity(this.entity.getUuid());
      return;
    }
    if (projectileContact.isCollision(this.entity.getUuid())) {

      gameController.removeEntity(this.entity.getUuid());

      for (UUID uuid :
          projectileContact.getCollisions(
              this.entity.getUuid())) { // TODO this is not a good way to change the health -_-
        Entity hitEntity = null;
        try {
          hitEntity = gameStore.getEntity(uuid); // TODO everything about this is gross.
        } catch (EntityNotFound e) {
          continue;
        }
        if (!(hitEntity instanceof Block)) {
          synchronized (
              hitEntity) { // TODO this isnt working i dont think. does it globally lock the entity?
            Health newHealth = new Health(hitEntity.getHealth().getValue() - 5);
            gameController.updateEntityAttribute(hitEntity.getUuid(), newHealth);
          }
          return;
        }
      }
    }
    if (!this.entity.coordinates.equals(moveTo))
      gameController.moveEntity(this.entity.getUuid(), moveTo);
  }
}

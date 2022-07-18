package entity.controllers;

import app.game.GameController;
import common.GameSettings;
import common.GameStore;
import common.events.EventService;
import entity.Entity;
import entity.attributes.inventory.FullBagException;
import entity.attributes.inventory.item.OrbInventoryItem;
import entity.attributes.msc.Coordinates;
import entity.collision.orb.OrbContact;
import entity.controllers.actions.EntityActionFactory;
import entity.misc.Orb;
import java.util.UUID;
import networking.events.EventTypeFactory;

public class OrbController extends EntityController {
  GameStore gameStore;
  OrbContact orbContact;

  public OrbController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      GameStore gameStore,
      OrbContact orbContact,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    this.orbContact = orbContact;
    this.gameStore = gameStore;
  }

  @Override
  public void afterWorldUpdate() throws Exception {

    if (orbContact.isCollision(this.entity.getUuid())) {
      for (UUID contact : orbContact.getCollisions(this.entity.getUuid())) {
        Entity entity = gameStore.getEntity(contact);
        if (entity.getBag().freeSpace() > 0) {
          try {
            entity.getBag().appendItem(new OrbInventoryItem(0));
          } catch (FullBagException e) {
            continue;
          }
          gameController.removeEntity(this.entity.getUuid());
          return;
        }
      }
      return;
    }

    if (!gameStore.doesChunkExist(entity.getChunk().chunkRange.getDown())) {
      /* If the chunk below doesn't exist. Don't move down. It could cause a problem */
      entity.setBodyPosition(entity.coordinates.toPhysicsVector2());
      return;
    }

    Coordinates moveTo =
        new Coordinates(
            this.entity.getBodyPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);
    if (!this.entity.coordinates.equals(moveTo)) {
      gameController.moveEntity(this.entity.getUuid(), moveTo);
      ((Orb) this.entity).needsUpdate();
    }
  }
}

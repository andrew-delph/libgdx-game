package entity.controllers;

import app.GameController;
import common.GameSettings;
import common.events.EventService;
import entity.Entity;
import entity.attributes.Coordinates;
import entity.collision.orb.OrbContact;
import entity.controllers.actions.EntityActionFactory;
import networking.events.EventTypeFactory;

public class OrbController extends EntityController {
  OrbContact orbContact;

  public OrbController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      OrbContact orbContact,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    this.orbContact = orbContact;
  }

  @Override
  public void afterWorldUpdate() throws Exception {

    if (orbContact.isCollision(this.entity.getUuid())) {
      gameController.removeEntity(this.entity.getUuid());
      return;
    }
    Coordinates moveTo =
        new Coordinates(
            this.entity.getBodyPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);
    if (!this.entity.coordinates.equals(moveTo))
      gameController.moveEntity(this.entity.getUuid(), moveTo);
  }
}

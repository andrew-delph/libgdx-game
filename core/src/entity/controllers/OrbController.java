package entity.controllers;

import app.GameController;
import common.GameSettings;
import common.events.EventService;
import entity.Entity;
import entity.attributes.Coordinates;
import entity.controllers.actions.EntityActionFactory;
import networking.events.EventTypeFactory;

public class OrbController extends EntityController {

  public OrbController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    Coordinates moveTo =
        new Coordinates(
            this.entity.getBodyPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);
    if (!this.entity.coordinates.equals(moveTo))
      gameController.moveEntity(this.entity.getUuid(), moveTo);
  }
}

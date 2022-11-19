package core.entity.controllers;

import core.app.game.GameController;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.networking.events.EventTypeFactory;

public class SolidBlockController extends EntityController {

  public SolidBlockController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void beforeWorldUpdate() throws Exception {}

  @Override
  public void afterWorldUpdate() throws Exception {}
}

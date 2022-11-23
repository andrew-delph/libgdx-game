package core.entity.controllers;

import core.app.game.GameController;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.networking.events.EventTypeFactory;

public class LadderController extends EntityController {

  public LadderController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    //    super.beforeWorldUpdate();
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    //    super.afterWorldUpdate();

    // if the ladder is not on a block. delete it
    try {
      gameStore.getBlock(entity.getCoordinatesWrapper().getCoordinates().getBase());
    } catch (EntityNotFound entityNotFound) {
      gameController.removeEntity(entity.getUuid());
    }
  }
}

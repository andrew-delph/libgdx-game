package core.entity.controllers;

import core.app.game.GameController;
import core.common.Coordinates;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.misc.water.WaterService;
import core.networking.events.EventTypeFactory;

public class WaterController extends EntityController {

  WaterService waterService;

  public WaterController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      WaterService waterService,
      Entity entity) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
    this.waterService = waterService;
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    super.beforeWorldUpdate();
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    super.afterWorldUpdate();
    // if this position isnt register then delete this entity
    Coordinates coordinates = this.entity.getCoordinatesWrapper().getCoordinates();
    if (!waterService.hasPosition(coordinates)) {
      gameController.removeEntity(this.entity.getUuid());
    }
  }
}

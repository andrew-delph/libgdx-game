package core.entity.controllers;

import core.app.game.GameController;
import core.common.Coordinates;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.misc.water.WaterService;
import core.networking.events.EventTypeFactory;

public class WaterPositionController extends EntityController {

  WaterService waterService;

  public WaterPositionController(
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
    // on this position create water and register it
    Coordinates coordinates = this.entity.getCoordinatesWrapper().getCoordinates();

    // TODO create a list for each of 4 coordposition. create water for each corner

    boolean hasWater = waterService.hasPosition(coordinates);

    if (!hasWater) {
      waterService.registerPosition(coordinates);
      gameController.createWater(coordinates);
    }
  }
}

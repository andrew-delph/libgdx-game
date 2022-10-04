package core.entity.controllers;

import core.app.game.GameController;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.misc.water.WaterService;
import core.networking.events.EventTypeFactory;
import java.util.LinkedList;
import java.util.List;

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
    //    entity.setBodyVelocity(entity.getBodyVelocity().scl(20, 1));
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    Coordinates moveTo =
        CommonFactory.createCoordinates(
            this.entity.getBodyPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);
    if (!this.entity.getCoordinatesWrapper().getCoordinates().equals(moveTo))
      gameController.moveEntity(this.entity.getUuid(), moveTo);

    Coordinates coordinates = this.entity.getCenter();

    List<Coordinates> touching = new LinkedList<>();

    touching.add(coordinates);

    for (Coordinates corner : touching) {
      corner = corner.getBase();
      waterService.notifyPosition(corner);
    }
  }
}

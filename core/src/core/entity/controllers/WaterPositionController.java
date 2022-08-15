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
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    super.afterWorldUpdate();
    // on this position create water and register it
    Coordinates coordinates = this.entity.getCoordinatesWrapper().getCoordinates();

    // TODO create a list for each of 4 coordposition. create water for each corner

    List<Coordinates> touching = new LinkedList<>();

    float theSize = entity.getHeight() / (float) GameSettings.PIXEL_SCALE;

    touching.add(coordinates); // bottom left
    touching.add(
        CommonFactory.createCoordinates(
            coordinates.getXReal(), coordinates.getYReal() + theSize)); // top left
    touching.add(
        CommonFactory.createCoordinates(
            coordinates.getXReal() + theSize, coordinates.getYReal() + theSize)); // top right
    touching.add(
        CommonFactory.createCoordinates(
            coordinates.getXReal() + theSize, coordinates.getYReal())); // bottom right

    for (Coordinates corner : touching) {
      //      System.out.println(corner);
      boolean hasWater = waterService.hasPosition(corner);
      if (!hasWater) {
        waterService.registerPosition(corner);
        gameController.createWater(corner);
      }
    }

    //    System.out.println();
  }
}

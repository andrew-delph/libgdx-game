package core.entity.controllers;

import core.app.game.GameController;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.pathfinding.PathGuider;
import core.entity.pathfinding.PathGuiderFactory;
import core.networking.events.EventTypeFactory;

public class EntityPathController extends EntityController {

  PathGuiderFactory pathGuiderFactory;
  EventService eventService;
  EventTypeFactory eventTypeFactory;
  EntityFactory entityFactory;
  PathGuider pathGuider;
  Entity target;
  Coordinates beforeUpdateCoordinates = null;

  public EntityPathController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      PathGuiderFactory pathGuiderFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      EntityFactory entityFactory,
      Entity entity,
      Entity target) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
    this.pathGuiderFactory = pathGuiderFactory;
    this.eventService = eventService;
    this.target = target;
    this.eventTypeFactory = eventTypeFactory;
    this.entityFactory = entityFactory;
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    super.afterWorldUpdate();
  }

  @Override
  public void render() {
    if (this.pathGuider != null) this.pathGuider.render();
  }

  @Override
  public void beforeWorldUpdate() {
    this.beforeUpdateCoordinates = this.entity.coordinates;
    if (!gameStore.doesEntityExist(target.getUuid())) {
      gameController.removeEntity(entity.getUuid());
      return;
    }
    if (this.pathGuider == null) {
      this.pathGuider = pathGuiderFactory.createPathGuider(entity);
    }
    if (this.entity.coordinates.getBase().calcDistance(target.coordinates) < 2) {
      gameController.useItem(entity);
    }
    try {
      this.pathGuider.followPath(target.coordinates);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

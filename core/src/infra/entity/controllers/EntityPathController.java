package infra.entity.controllers;

import infra.app.GameController;
import infra.common.Coordinates;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.actions.EntityActionFactory;
import infra.entity.pathfinding.PathGuider;
import infra.entity.pathfinding.PathGuiderFactory;
import infra.networking.events.EventFactory;

public class EntityPathController extends EntityController {

  PathGuiderFactory pathGuiderFactory;

  EventService eventService;
  EventFactory eventFactory;
  EntityFactory entityFactory;

  PathGuider pathGuider;
  Entity target;
  boolean moved = true;
  Coordinates beforeUpdateCoordinates = null;

  EntityPathController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      PathGuiderFactory pathGuiderFactory,
      EventService eventService,
      EventFactory eventFactory,
      EntityFactory entityFactory,
      Entity entity,
      Entity target) {
    super(gameController, entityActionFactory, entity);
    this.pathGuiderFactory = pathGuiderFactory;
    this.eventService = eventService;
    this.target = target;
    this.eventFactory = eventFactory;
    this.entityFactory = entityFactory;
  }

  @Override
  public void afterWorldUpdate() {
    super.afterWorldUpdate();
      moved = !this.beforeUpdateCoordinates.equals(this.entity.coordinates);
  }

  @Override
  public void beforeWorldUpdate() {
    this.beforeUpdateCoordinates = this.entity.coordinates;

    if (this.pathGuider == null) {
      this.pathGuider = pathGuiderFactory.createPathGuider(entity);
      try {
        this.pathGuider.findPath(this.entity.coordinates, target.coordinates);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (!this.pathGuider.hasPath()) {
      eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
      this.entity = this.entityFactory.createEntity();
      this.entity.coordinates = new Coordinates(0, 1);
      this.entity.setController(this);
      this.gameController.createEntity(this.entity);
      this.pathGuider = null;
      return;
    }

    if (this.pathGuider.hasPath()) {
      this.pathGuider.followPath();
    } else {
      System.out.println("NO path");
    }
  }
}

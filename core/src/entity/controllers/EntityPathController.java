package entity.controllers;

import app.GameController;
import common.Coordinates;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.PathGuider;
import entity.pathfinding.PathGuiderFactory;
import networking.events.EventFactory;

public class EntityPathController extends EntityController {

  PathGuiderFactory pathGuiderFactory;

  EventService eventService;
  EventFactory eventFactory;
  EntityFactory entityFactory;

  PathGuider pathGuider;
  Entity target;
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
  }

  @Override
  public void beforeWorldUpdate() {
    this.beforeUpdateCoordinates = this.entity.coordinates;
    if (this.pathGuider == null) {
      this.pathGuider = pathGuiderFactory.createPathGuider(entity);
    }

    if (this.entity.coordinates.getBase().equals(target.coordinates.getBase())) {
      eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
      return;
    }
    try {
      this.pathGuider.followPath(target.coordinates);
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    //    if (!this.pathGuider.hasPath()) {
    //      if (entity.coordinates.getBase().equals(target.coordinates.getBase())) {
    //        eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
    //        //        this.entity = this.entityFactory.createEntity();
    //        //        this.entity.coordinates = new Coordinates(0, 1);
    //        //        this.entity.setController(this);
    //        //        this.gameController.createEntity(this.entity);
    //        this.pathGuider = null;
    //      } else {
    //        try {
    //          this.pathGuider.findPath(entity.coordinates, target.coordinates);
    //        } catch (Exception e) {
    //          e.printStackTrace();
    //        }
    //      }
    //      return;
    //    }
    //
    //    if (this.pathGuider.hasPath()) {
    //
    //    } else {
    //      System.out.println("NO path");
    //    }
  }
}

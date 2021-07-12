package infra.entity.controllers;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.app.GameController;
import infra.common.Coordinates;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.actions.EntityActionFactory;
import infra.entity.pathfinding.template.PathGuider;
import infra.entity.pathfinding.template.PathGuiderFactory;
import infra.networking.events.EventFactory;

public class EntityPathController extends EntityController {

  @Inject GameController gameController;

  @Inject PathGuiderFactory pathGuiderFactory;

  @Inject EventService eventService;
  @Inject EventFactory eventFactory;
  @Inject EntityFactory entityFactory;

  PathGuider pathGuider;
  Entity target;

  @Inject
  EntityPathController(
      EntityActionFactory entityActionFactory,
      @Assisted("source") Entity entity,
      @Assisted("target") Entity target) {
    super(entityActionFactory, entity);
    this.target = target;
  }

  @Override
  public void beforeWorldUpdate() {

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

  @Override
  public void afterWorldUpdate() {
    gameController.moveEntity(
        this.entity.uuid,
        new Coordinates(
            this.entity.getBody().getPosition().x / Entity.coordinatesScale,
            this.entity.getBody().getPosition().y / Entity.coordinatesScale));
  }
}

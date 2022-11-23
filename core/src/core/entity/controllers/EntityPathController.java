package core.entity.controllers;

import core.app.game.GameController;
import core.app.screen.assets.animations.AnimationState;
import core.common.Coordinates;
import core.common.Direction;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.DirectionWrapper;
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
    this.beforeUpdateCoordinates = this.entity.getCoordinatesWrapper().getCoordinates();
    if (!gameStore.doesEntityExist(target.getUuid())) {
      gameController.removeEntity(entity.getUuid());
      return;
    }
    if (this.pathGuider == null) {
      this.pathGuider = pathGuiderFactory.createPathGuider(entity);
    }
    if (this.entity
            .getCoordinatesWrapper()
            .getCoordinates()
            .getBase()
            .calcDistance(target.getCoordinatesWrapper().getCoordinates())
        < 1) {

      if (this.entity
              .getCoordinatesWrapper()
              .getCoordinates()
              .calcDifference(target.getCoordinatesWrapper().getCoordinates())[0]
          < 0) {
        try {
          // this is not good but lol
          this.gameController.updateEntityAttribute(
              entity.getUuid(), new DirectionWrapper(Direction.LEFT));
        } catch (EntityNotFound e) {
          e.printStackTrace();
        }
        this.entity.getEntityStateMachine().attemptTransition(AnimationState.PUNCH_LEFT);
      } else {
        try {
          // this is not good but lol
          this.gameController.updateEntityAttribute(
              entity.getUuid(), new DirectionWrapper(Direction.RIGHT));
        } catch (EntityNotFound e) {
          e.printStackTrace();
        }
        this.entity.getEntityStateMachine().attemptTransition(AnimationState.PUNCH_RIGHT);
      }
    }
    try {
      this.pathGuider.followPath(target.getCoordinatesWrapper().getCoordinates());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

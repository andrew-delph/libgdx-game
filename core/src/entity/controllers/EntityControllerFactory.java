package entity.controllers;

import app.GameController;
import com.google.inject.Inject;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.PathGuiderFactory;
import networking.events.EventFactory;

public class EntityControllerFactory {
  @Inject GameController gameController;
  @Inject EntityActionFactory entityActionFactory;
  @Inject PathGuiderFactory pathGuiderFactory;
  @Inject EventService eventService;
  @Inject EntityFactory entityFactory;
  @Inject EventFactory eventFactory;

  @Inject
  public EntityControllerFactory() {}

  public EntityUserController createEntityUserController(Entity entity) {
    return new EntityUserController(
        gameController, entityActionFactory, eventService, eventFactory, entity);
  }

  public EntityPathController createEntityPathController(Entity source, Entity target) {
    return new EntityPathController(
        gameController,
        entityActionFactory,
        pathGuiderFactory,
        eventService,
        eventFactory,
        entityFactory,
        source,
        target);
  }
}

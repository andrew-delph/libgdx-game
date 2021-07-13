package infra.entity.controllers;


import com.google.inject.Inject;

import infra.app.GameController;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.actions.EntityActionFactory;
import infra.entity.pathfinding.template.PathGuiderFactory;
import infra.networking.events.EventFactory;

public class EntityControllerFactory {
  @Inject
  GameController gameController;
  @Inject
  EntityActionFactory entityActionFactory;
  @Inject
  PathGuiderFactory pathGuiderFactory;
  @Inject
  EventService eventService;
  @Inject
  EntityFactory entityFactory;
  @Inject
  EventFactory eventFactory;
  @Inject
  public EntityControllerFactory(){

  }
  public EntityUserController createEntityUserController(Entity entity){
    return new EntityUserController(gameController,entityActionFactory, entity);
  };

  public EntityPathController createEntityPathController(Entity source, Entity target){
    return new EntityPathController(gameController, entityActionFactory,pathGuiderFactory,eventService,eventFactory,entityFactory, source, target);
  }
}

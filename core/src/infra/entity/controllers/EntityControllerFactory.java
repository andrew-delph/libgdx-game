package infra.entity.controllers;

import com.google.inject.Inject;

import infra.app.GameController;
import infra.entity.Entity;

public class EntityControllerFactory {
  @Inject
  GameController gameController;
  public EntityUserController createEntityUserController(Entity entity){
    return new EntityUserController(gameController, entity);
  };
}

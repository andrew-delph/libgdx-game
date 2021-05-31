package infra.entity.controllers;

import infra.entity.Entity;

public interface EntityControllerFactory {
  EntityUserController createEntityUserController(Entity entity);
}

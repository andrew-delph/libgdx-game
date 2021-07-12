package infra.entity.controllers;

import com.google.inject.assistedinject.Assisted;
import infra.entity.Entity;

public interface EntityControllerFactory {
  EntityUserController createEntityUserController(Entity entity);

  EntityController createEntityController(Entity entity);

  EntityPathController createEntityPathController(
      @Assisted("source") Entity entity, @Assisted("target") Entity target);
}

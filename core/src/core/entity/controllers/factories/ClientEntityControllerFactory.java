package core.entity.controllers.factories;

import core.entity.Entity;
import core.entity.controllers.EntityController;

public class ClientEntityControllerFactory extends EntityControllerFactory {

  @Override
  public EntityController createLadderController(Entity entity) {
    return this.createRemoteBodyController(entity);
  }
}

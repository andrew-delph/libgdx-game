package core.entity.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import core.app.game.GameController;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.networking.events.EventTypeFactory;

public class RemoteBodyController extends EntityController {

  public RemoteBodyController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    if (this.entity.hasBody())
      this.entity.applyBody(
          (Body body) -> {
            body.setTransform(
                this.entity.getCoordinatesWrapper().getCoordinates().toPhysicsVector2(), 0);
          });
  }

  @Override
  public void afterWorldUpdate() throws Exception {}
}

package entity.controllers;

import app.GameController;
import com.badlogic.gdx.physics.box2d.Body;
import common.events.EventService;
import entity.Entity;
import entity.controllers.actions.EntityActionFactory;
import networking.events.EventTypeFactory;

public class RemoteBodyController extends EntityController {

  public RemoteBodyController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    this.entity.applyBody(
        (Body body) -> {
          body.setTransform(this.entity.coordinates.toVector2(), 0);
        });
  }

  @Override
  public void afterWorldUpdate() throws Exception {}
}

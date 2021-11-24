package entity.controllers;

import app.GameController;
import com.badlogic.gdx.physics.box2d.Body;
import common.Coordinates;
import common.events.EventService;
import entity.Entity;
import entity.controllers.actions.EntityAction;
import entity.controllers.actions.EntityActionFactory;
import networking.events.EventTypeFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityController {
  GameController gameController;
  EventService eventService;
  EventTypeFactory eventTypeFactory;
  Entity entity;
  Map<String, EntityAction> actionMap;

  public EntityController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    this.gameController = gameController;
    this.eventService = eventService;
    this.eventTypeFactory = eventTypeFactory;
    this.entity = entity;
    this.actionMap = new HashMap<>();

    this.registerAction("left", entityActionFactory.createHorizontalMovementAction(-5));
    this.registerAction("right", entityActionFactory.createHorizontalMovementAction(5));
    this.registerAction("jump", entityActionFactory.createJumpMovementAction());
    this.registerAction("stop", entityActionFactory.createStopMovementAction());
    this.registerAction("climbUp", entityActionFactory.createClimbUpMovementAction());
    this.registerAction("climbDown", entityActionFactory.createClimbDownMovementAction());
  }

  public void registerAction(String type, EntityAction action) {
    this.actionMap.put(type, action);
  }

  public void applyAction(String type, Body body) {
    this.actionMap.get(type).apply(body);
  }

  public EntityAction getAction(String type) {
    return this.actionMap.get(type);
  }

  public Set<Map.Entry<String, EntityAction>> getEntityActionEntrySet() {
    return this.actionMap.entrySet();
  }

  public void beforeWorldUpdate() {}

  public void afterWorldUpdate() {

    gameController.moveEntity(
        this.entity.uuid,
        new Coordinates(
            this.entity.getBody().getPosition().x / Entity.coordinatesScale,
            this.entity.getBody().getPosition().y / Entity.coordinatesScale));
    if (this.getAction("climbUp").isValid(this.entity.getBody())) {
      this.entity.getBody().setLinearVelocity(0, 0);
    } else {
      this.entity.getBody().setLinearVelocity(0, this.entity.getBody().getLinearVelocity().y);
    }
  }
}

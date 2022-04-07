package entity.controllers;

import app.GameController;
import com.badlogic.gdx.physics.box2d.Body;
import common.Coordinates;
import common.GameSettings;
import common.events.EventService;
import entity.Entity;
import entity.controllers.actions.EntityAction;
import entity.controllers.actions.EntityActionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import networking.events.EventTypeFactory;

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

  public Boolean isActionValid(String type, Body body) {
    return this.actionMap.get(type).isValid(body);
  }

  public Set<Map.Entry<String, EntityAction>> getEntityActionEntrySet() {
    return this.actionMap.entrySet();
  }

  public void beforeWorldUpdate() throws Exception {}

  public void afterWorldUpdate() throws Exception {
    Coordinates moveTo =
        new Coordinates(
            this.entity.getBody().getPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBody().getPosition().y / GameSettings.PHYSICS_SCALE);
    if (!this.entity.coordinates.equals(moveTo))
      gameController.moveEntity(this.entity.uuid, moveTo);

    if (this.getAction("climbUp").isValid(this.entity.getBody())) {
      this.entity.getBody().setLinearVelocity(0, 0);
      this.entity.getBody().setGravityScale(0);
    } else {
      this.entity.getBody().setGravityScale(1);
      this.entity.getBody().setLinearVelocity(0, this.entity.getBody().getLinearVelocity().y);
    }
  }

  public void render() {}
}

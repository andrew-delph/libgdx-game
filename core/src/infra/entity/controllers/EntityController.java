package infra.entity.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import infra.app.GameController;
import infra.common.Coordinates;
import infra.app.GameController;
import infra.entity.Entity;
import infra.entity.controllers.actions.EntityAction;
import infra.entity.controllers.actions.EntityActionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityController {
  GameController gameController;
  Entity entity;
  Map<String, EntityAction> actionMap;


  public EntityController(GameController gameController,EntityActionFactory entityActionFactory, Entity entity) {
    this.gameController =gameController;
    this.entity = entity;
    this.actionMap = new HashMap<>();

    this.registerAction("left", entityActionFactory.createHorizontalMovementAction(-5));
    this.registerAction("right", entityActionFactory.createHorizontalMovementAction(5));
    this.registerAction("jump", entityActionFactory.createJumpMovementAction());
    this.registerAction("stop", entityActionFactory.createStopMovementAction());
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
    this.entity.getBody().setLinearVelocity(0, this.entity.getBody().getLinearVelocity().y);
  }
}

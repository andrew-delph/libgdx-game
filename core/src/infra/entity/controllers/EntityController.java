package infra.entity.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import infra.entity.Entity;
import infra.entity.controllers.actions.EntityAction;
import infra.entity.controllers.actions.HorizontalMovementAction;
import infra.entity.controllers.actions.JumpMovementAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityController {
  Entity entity;

  Map<String, EntityAction> actionMap;

  public EntityController(Entity entity) {
    this.entity = entity;
    this.actionMap = new HashMap<>();

    this.registerAction("left", new HorizontalMovementAction(-5));
    this.registerAction("right", new HorizontalMovementAction(5));
    this.registerAction("jump", new JumpMovementAction());
  }

  public void registerAction(String type, EntityAction action) {
    this.actionMap.put(type, action);
  }

  public void applyAction(String type, Body body) {
    this.actionMap.get(type).apply(body);
  }

  public Set<Map.Entry<String, EntityAction>> getEntityActionEntrySet() {
    return this.actionMap.entrySet();
  }

  public void beforeWorldUpdate() {}

  public void afterWorldUpdate() {}
}

package infra.entity.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import infra.entity.Entity;
import infra.entity.controllers.actions.EntityAction;

import java.util.HashMap;
import java.util.Map;

public class EntityController {
  Entity entity;

  Map<String, EntityAction> actionMap;

  public EntityController(Entity entity) {
    this.entity = entity;
    this.actionMap = new HashMap<>();
  }

  public void registerAction(String type, EntityAction action){
    this.actionMap.put(type, action);
  }

  public void applyAction(String type, Body body){
    this.actionMap.get(type).apply(body);
  }

  public void beforeWorldUpdate() {}

  public void afterWorldUpdate() {}
}

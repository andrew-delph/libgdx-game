package infra.entity.controllers;

import infra.app.GameController;
import infra.entity.Entity;

public class EntityController {
  GameController gameController;
  Entity entity;

  public EntityController(GameController gameController, Entity entity) {
    this.gameController =gameController;
    this.entity = entity;
  }

  public void beforeWorldUpdate() {}

  public void afterWorldUpdate() {}
}

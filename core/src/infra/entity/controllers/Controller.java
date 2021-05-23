package infra.entity.controllers;

import infra.entity.Entity;

public class Controller {
  Entity entity;
  public Controller(Entity entity) {
    this.entity = entity;
  }

  public void update() {}
}

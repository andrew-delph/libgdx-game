package entity.controllers;

import app.GameController;
import common.Clock;
import common.GameStore;
import common.Tick;
import common.Util;
import common.events.EventService;
import entity.Entity;
import entity.collision.RayCastService;
import entity.controllers.actions.EntityActionFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import networking.events.EventTypeFactory;

public class TurretController extends EntityController {

  Tick tick;
  GameStore gameStore;
  Clock clock;
  RayCastService rayCastService;

  public TurretController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Clock clock,
      GameStore gameStore,
      RayCastService rayCastService,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    this.gameStore = gameStore;
    this.clock = clock;
    this.rayCastService = rayCastService;
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    super.beforeWorldUpdate();
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    // get entities surrounding. let's say 7 coordinates in space.
    // if the entity is of type Entity. ray cast and check if we can shoot it.
    if (tick != null && clock.getCurrentTick().time - tick.time < 10) return;

    List<Entity> closeEntities = gameStore.getEntityInRange(entity.coordinates, 6);

    // filter out entities

    closeEntities.sort(
        Comparator.comparingDouble((Entity o) -> o.coordinates.calcDistance(entity.coordinates)));

    for (Entity next : closeEntities) {
      Set<Entity> rayCastEntities = rayCastService.rayCast(entity.coordinates, next.coordinates);
      if (!next.getClass().equals(Entity.class)) continue;
      rayCastEntities.remove(entity);
      rayCastEntities.remove(next);

      if (rayCastEntities.size() == 0) {
        gameController.createProjectile(
            entity.coordinates, Util.calcVelocity(entity.coordinates, next.coordinates, 5));
        tick = clock.getCurrentTick();
        return;
      }
    }
  }
}

package entity.controllers;

import app.GameController;
import common.Clock;
import common.GameStore;
import common.Tick;
import common.Util;
import common.events.EventService;
import entity.Entity;
import entity.block.SolidBlock;
import entity.collision.RayCastService;
import entity.controllers.actions.EntityActionFactory;
import entity.groups.Group;
import entity.groups.GroupService;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import networking.events.EventTypeFactory;

public class TurretController extends EntityController {

  Tick tick;
  GameStore gameStore;
  Clock clock;
  RayCastService rayCastService;
  GroupService groupService;

  public TurretController(
      GameController gameController,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Clock clock,
      GameStore gameStore,
      RayCastService rayCastService,
      GroupService groupService,
      Entity entity) {
    super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    this.gameStore = gameStore;
    this.clock = clock;
    this.rayCastService = rayCastService;
    this.groupService = groupService;
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    super.beforeWorldUpdate();
  }

  @Override
  public void afterWorldUpdate() throws Exception {
    // get entities surrounding. let's say 7 coordinates in space.
    // if the entity is of type Entity. ray cast and check if we can shoot it.
    if (tick != null && clock.getCurrentTick().time - tick.time < 50) return;

    List<Entity> closeEntities = gameStore.getEntityInRange(entity.coordinates, 15);

    // filter out entities

    closeEntities =
        closeEntities.stream()
            .filter((e) -> groupService.getGroup(e.getUuid()) == Group.AI_GROUP)
            .collect(Collectors.toList());

    closeEntities.sort(
        Comparator.comparingDouble((Entity o) -> o.coordinates.calcDistance(entity.coordinates)));

    for (Entity next : closeEntities) {
      Set<Entity> rayCastEntities = rayCastService.rayCast(entity.coordinates, next.coordinates);
      if (!next.getClass().equals(Entity.class)) continue;
      rayCastEntities.remove(entity);
      rayCastEntities.remove(next);

      rayCastEntities.removeIf(e -> !(e instanceof SolidBlock));

      if (rayCastEntities.size() == 0) {
        gameController.createProjectile(
            entity.coordinates, Util.calcVelocity(entity.coordinates, next.coordinates, 5));
        tick = clock.getCurrentTick();
        return;
      }
    }
  }
}

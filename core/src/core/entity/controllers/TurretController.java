package core.entity.controllers;

import core.app.game.GameController;
import core.common.Clock;
import core.common.GameStore;
import core.common.Tick;
import core.common.Util;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.block.SolidBlock;
import core.entity.collision.RayCastService;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.groups.Group;
import core.entity.groups.GroupService;
import core.networking.events.EventTypeFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TurretController extends EntityController {

  Tick tick;
  GameStore gameStore;
  Clock clock;
  RayCastService rayCastService;
  GroupService groupService;

  public TurretController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Clock clock,
      RayCastService rayCastService,
      GroupService groupService,
      Entity entity) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
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

    List<Entity> closeEntities =
        gameStore.getEntityInRange(entity.getCoordinatesWrapper().getCoordinates(), 15);

    // filter out entities

    closeEntities =
        closeEntities.stream()
            .filter((e) -> groupService.getGroup(e.getUuid()) == Group.AI_GROUP)
            .collect(Collectors.toList());

    closeEntities.sort(
        Comparator.comparingDouble(
            (Entity o) ->
                o.getCoordinatesWrapper()
                    .getCoordinates()
                    .calcDistance(entity.getCoordinatesWrapper().getCoordinates())));

    for (Entity next : closeEntities) {
      Set<Entity> rayCastEntities =
          rayCastService.rayCast(
              entity.getCoordinatesWrapper().getCoordinates(),
              next.getCoordinatesWrapper().getCoordinates());
      if (!next.getClass().equals(Entity.class)) continue;
      rayCastEntities.remove(entity);
      rayCastEntities.remove(next);

      rayCastEntities.removeIf(e -> !(e instanceof SolidBlock));

      if (rayCastEntities.size() == 0) {
        gameController.createProjectile(
            entity.getCoordinatesWrapper().getCoordinates(),
            Util.calcVelocity(
                entity.getCoordinatesWrapper().getCoordinates(),
                next.getCoordinatesWrapper().getCoordinates(),
                5));
        tick = clock.getCurrentTick();
        return;
      }
    }
  }
}

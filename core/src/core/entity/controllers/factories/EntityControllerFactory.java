package core.entity.controllers.factories;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.collision.RayCastService;
import core.entity.collision.orb.OrbContact;
import core.entity.collision.projectile.ProjectileContact;
import core.entity.controllers.EntityController;
import core.entity.controllers.EntityPathController;
import core.entity.controllers.EntityUserController;
import core.entity.controllers.OrbController;
import core.entity.controllers.ProjectileController;
import core.entity.controllers.RemoteBodyController;
import core.entity.controllers.SandController;
import core.entity.controllers.TurretController;
import core.entity.controllers.WaterController;
import core.entity.controllers.WaterPositionController;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.groups.GroupService;
import core.entity.misc.water.WaterService;
import core.entity.pathfinding.PathGuiderFactory;
import core.networking.events.EventTypeFactory;

public abstract class EntityControllerFactory {
  @Inject GameController gameController;
  @Inject EntityActionFactory entityActionFactory;
  @Inject PathGuiderFactory pathGuiderFactory;
  @Inject EventService eventService;
  @Inject EntityFactory entityFactory;
  @Inject EventTypeFactory eventTypeFactory;
  @Inject GameStore gameStore;
  @Inject Clock clock;
  @Inject RayCastService rayCastService;
  @Inject ProjectileContact projectileContact;
  @Inject OrbContact orbContact;
  @Inject GroupService groupService;
  @Inject WaterService waterService;

  public EntityControllerFactory() {}

  public EntityController createEntityUserController(Entity entity) {
    return new EntityUserController(
        gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public EntityController createEntityPathController(Entity source, Entity target) {
    return (new EntityPathController(
        gameController,
        gameStore,
        entityActionFactory,
        pathGuiderFactory,
        eventService,
        eventTypeFactory,
        entityFactory,
        source,
        target));
  }

  public EntityController createProjectileController(
      Entity entity, Coordinates startPosition, float travelDistance) {
    return new ProjectileController(
        gameController,
        gameStore,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        entity,
        projectileContact,
        startPosition,
        travelDistance);
  }

  public EntityController createTurretController(Entity entity) {
    return new TurretController(
        gameController,
        gameStore,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        clock,
        rayCastService,
        groupService,
        entity);
  }

  public EntityController createRemoteBodyController(Entity entity) {
    return new RemoteBodyController(
        gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public EntityController createOrbController(Entity entity) {
    return new OrbController(
        gameController,
        gameStore,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        orbContact,
        entity);
  }

  public EntityController createEntityController(Entity entity) {
    return new EntityController(
        gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public EntityController createWaterPositionController(Entity entity) {
    return new WaterPositionController(
        gameController,
        gameStore,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        waterService,
        entity);
  }

  public EntityController createWaterController(Entity entity) {
    return new WaterController(
        gameController,
        gameStore,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        waterService,
        entity);
  }

  public EntityController createSandController(Entity entity) {
    return new SandController(
        gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }
}

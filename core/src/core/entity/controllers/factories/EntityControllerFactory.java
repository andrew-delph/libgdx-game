package core.entity.controllers.factories;

import core.app.game.GameController;
import com.google.inject.Inject;
import core.common.Clock;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.collision.RayCastService;
import core.entity.controllers.EntityController;
import core.entity.controllers.EntityPathController;
import core.entity.controllers.EntityUserController;
import core.entity.controllers.ProjectileController;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.collision.orb.OrbContact;
import core.entity.collision.projectile.ProjectileContact;
import core.entity.controllers.OrbController;
import core.entity.controllers.RemoteBodyController;
import core.entity.controllers.TurretController;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.groups.GroupService;
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
  @Inject
  RayCastService rayCastService;
  @Inject ProjectileContact projectileContact;
  @Inject OrbContact orbContact;
  @Inject GroupService groupService;

  public EntityControllerFactory() {}

  public EntityController createEntityUserController(Entity entity) {
    return new EntityUserController(
        gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public EntityController createEntityPathController(Entity source, Entity target) {
    return (new EntityPathController(
        gameController,
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
        entityActionFactory,
        eventService,
        eventTypeFactory,
        gameStore,
        entity,
        projectileContact,
        startPosition,
        travelDistance);
  }

  public EntityController createTurretController(Entity entity) {
    return new TurretController(
        gameController,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        clock,
        gameStore,
        rayCastService,
        groupService,
        entity);
  }

  public EntityController createRemoteBodyController(Entity entity) {
    return new RemoteBodyController(
        gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public EntityController createOrbController(Entity entity) {
    return new OrbController(
        gameController,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        gameStore,
        orbContact,
        entity);
  }
}

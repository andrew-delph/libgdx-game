package entity.controllers.factories;

import app.GameController;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.msc.Coordinates;
import entity.collision.RayCastService;
import entity.collision.orb.OrbContact;
import entity.collision.projectile.ProjectileContact;
import entity.controllers.EntityController;
import entity.controllers.EntityPathController;
import entity.controllers.EntityUserController;
import entity.controllers.OrbController;
import entity.controllers.ProjectileController;
import entity.controllers.RemoteBodyController;
import entity.controllers.TurretController;
import entity.controllers.actions.EntityActionFactory;
import entity.groups.GroupService;
import entity.pathfinding.PathGuiderFactory;
import networking.events.EventTypeFactory;

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

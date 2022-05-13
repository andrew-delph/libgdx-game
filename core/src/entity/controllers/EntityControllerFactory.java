package entity.controllers;

import app.GameController;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.Coordinates;
import entity.collision.RayCastService;
import entity.collision.projectile.ProjectileContact;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.PathGuiderFactory;
import networking.events.EventTypeFactory;

public class EntityControllerFactory {
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

  public EntityControllerFactory() {}

  public EntityUserController createEntityUserController(Entity entity) {
    return new EntityUserController(
        gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public EntityPathController createEntityPathController(Entity source, Entity target) {
    return new EntityPathController(
        gameController,
        entityActionFactory,
        pathGuiderFactory,
        eventService,
        eventTypeFactory,
        entityFactory,
        source,
        target);
  }

  public ProjectileController createProjectileController(
      Entity entity, Coordinates startPosition, float travelDistance) {
    return new ProjectileController(
        gameController,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        entity,
        projectileContact,
        startPosition,
        travelDistance);
  }

  public TurretController createTurretController(Entity entity) {
    return new TurretController(
        gameController,
        entityActionFactory,
        eventService,
        eventTypeFactory,
        clock,
        gameStore,
        rayCastService,
        entity);
  }

  public RemoteBodyController createRemoteBodyController(Entity entity) {
    return new RemoteBodyController(
        gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }
}

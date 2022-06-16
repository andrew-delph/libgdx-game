package entity.controllers.factories;

import app.GameController;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.Coordinates;
import entity.collision.RayCastService;
import entity.collision.orb.OrbContact;
import entity.collision.projectile.ProjectileContact;
import entity.controllers.EntityPathController;
import entity.controllers.EntityUserController;
import entity.controllers.OrbController;
import entity.controllers.ProjectileController;
import entity.controllers.RemoteBodyController;
import entity.controllers.TurretController;
import entity.controllers.actions.EntityActionFactory;
import entity.controllers.events.consumers.ChangedHealthConsumer;
import entity.controllers.events.consumers.FallDamageConsumer;
import entity.controllers.events.types.ChangeHealthEventType;
import entity.controllers.events.types.FallDamageEventType;
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

  @Inject FallDamageConsumer fallDamageConsumer;
  @Inject ChangedHealthConsumer changeHealthConsumer;

  public EntityControllerFactory() {}

  public EntityUserController createEntityUserController(Entity entity) {
    EntityUserController controller =
        new EntityUserController(
            gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    controller.registerEntityEventConsumer(FallDamageEventType.type, fallDamageConsumer);
    controller.registerEntityEventConsumer(ChangeHealthEventType.type, changeHealthConsumer);
    return controller;
  }

  public EntityPathController createEntityPathController(Entity source, Entity target) {
    EntityPathController controller =
        new EntityPathController(
            gameController,
            entityActionFactory,
            pathGuiderFactory,
            eventService,
            eventTypeFactory,
            entityFactory,
            source,
            target);
    controller.registerEntityEventConsumer(FallDamageEventType.type, fallDamageConsumer);
    controller.registerEntityEventConsumer(ChangeHealthEventType.type, changeHealthConsumer);
    return controller;
  }

  public ProjectileController createProjectileController(
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

  public TurretController createTurretController(Entity entity) {
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

  public RemoteBodyController createRemoteBodyController(Entity entity) {
    return new RemoteBodyController(
        gameController, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  public OrbController createOrbController(Entity entity) {
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

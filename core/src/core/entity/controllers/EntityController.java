package core.entity.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import core.app.game.GameController;
import core.app.screen.assets.animations.AnimationState;
import core.chunk.world.exceptions.BodyNotFound;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.controllers.actions.EntityAction;
import core.entity.controllers.actions.EntityActionFactory;
import core.entity.controllers.events.consumers.EntityEventConsumer;
import core.entity.controllers.events.types.AbstractEntityEventType;
import core.networking.events.EventTypeFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityController {
  GameController gameController;
  GameStore gameStore;
  EventService eventService;
  EventTypeFactory eventTypeFactory;
  Entity entity;
  Map<String, EntityAction> actionMap = new HashMap<>();
  Map<String, EntityEventConsumer> consumerMap = new HashMap<>();

  public EntityController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    this.gameController = gameController;
    this.gameStore = gameStore;
    this.eventService = eventService;
    this.eventTypeFactory = eventTypeFactory;
    this.entity = entity;

    this.registerAction("left", entityActionFactory.createHorizontalMovementAction(-5));
    this.registerAction("right", entityActionFactory.createHorizontalMovementAction(5));
    this.registerAction("jump", entityActionFactory.createJumpMovementAction());
    this.registerAction("stop", entityActionFactory.createStopMovementAction());
    this.registerAction("climbUp", entityActionFactory.createClimbUpMovementAction());
    this.registerAction("climbDown", entityActionFactory.createClimbDownMovementAction());
  }

  public EntityController registerAction(String type, EntityAction action) {
    this.actionMap.put(type, action);
    return this;
  }

  public EntityController registerEntityEventConsumer(String type, EntityEventConsumer consumer) {
    consumerMap.put(type, consumer);
    return this;
  }

  public void applyAction(String type, Entity entity) throws ChunkNotFound, BodyNotFound {
    this.actionMap.get(type).apply(entity);
  }

  public EntityAction getAction(String type) {
    return this.actionMap.get(type);
  }

  public Boolean isActionValid(String type, Entity entity) throws ChunkNotFound {
    return this.actionMap.get(type).isValid(entity);
  }

  public Set<Map.Entry<String, EntityAction>> getEntityActionEntrySet() {
    return this.actionMap.entrySet();
  }

  public void beforeWorldUpdate() throws Exception {}

  public void afterWorldUpdate() throws Exception {
    entity.getEntityStateMachine().callAnimation();
    entity.getEntityStateMachine().callAction();
    Coordinates moveTo =
        CommonFactory.createCoordinates(
            this.entity.getBodyPosition().x / GameSettings.PHYSICS_SCALE,
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);
    if (!this.entity.getCoordinatesWrapper().getCoordinates().equals(moveTo))
      gameController.moveEntity(this.entity.getUuid(), moveTo);
    else {
      entity.getEntityStateMachine().attemptTransition(AnimationState.DEFAULT);
    }

    if (this.getAction("climbUp").isValid(entity)) {
      this.entity.setBodyVelocity(new Vector2(0, 0));
      this.entity.applyBody(
          (Body body) -> {
            body.setGravityScale(0);
          });
    } else {
      this.entity.applyBody(
          (Body body) -> {
            body.setGravityScale(1);
          });
      // remove for attack booping
      //      this.entity.setBodyVelocity(new Vector2(0, this.entity.getBodyVelocity().y));
    }
  }

  public void fireEvent(AbstractEntityEventType event) {
    if (consumerMap.get(event.getEntityEventType()) != null)
      consumerMap.get(event.getEntityEventType()).accept(event);
  }

  public void render() {}
}

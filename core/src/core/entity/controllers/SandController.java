package core.entity.controllers;

import com.badlogic.gdx.math.Vector2;
import core.app.game.GameController;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.GameStore;
import core.common.events.EventService;
import core.entity.Entity;
import core.entity.controllers.actions.EntityActionFactory;
import core.networking.events.EventTypeFactory;

public class SandController extends EntityController {

  public SandController(
      GameController gameController,
      GameStore gameStore,
      EntityActionFactory entityActionFactory,
      EventService eventService,
      EventTypeFactory eventTypeFactory,
      Entity entity) {
    super(gameController, gameStore, entityActionFactory, eventService, eventTypeFactory, entity);
  }

  @Override
  public void beforeWorldUpdate() throws Exception {
    super.beforeWorldUpdate();
  }

  @Override
  public void afterWorldUpdate() throws Exception {

    Coordinates moveTo =
        CommonFactory.createCoordinates(
            this.entity.getCoordinatesWrapper().getCoordinates().getX(),
            this.entity.getBodyPosition().y / GameSettings.PHYSICS_SCALE);

    if (!this.entity.getCoordinatesWrapper().getCoordinates().equals(moveTo))
      gameController.moveEntity(this.entity.getUuid(), moveTo);

    entity.setBodyPosition(entity.getCoordinatesWrapper().getCoordinates().toPhysicsVector2());
    entity.setBodyVelocity(new Vector2(0, entity.getBodyVelocity().y));
  }
}

package core.entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;
import core.entity.collision.left.EntityLeftContact;
import core.entity.collision.right.EntityRightContact;

public class HorizontalMovementAction implements EntityAction {

  int magnitude;
  EntityLeftContact entityLeftContact;
  EntityRightContact entityRightContact;
  GameController gameController;

  @Inject
  HorizontalMovementAction(
      GameController gameController,
      EntityLeftContact entityLeftContact,
      EntityRightContact entityRightContact,
      int magnitude) {
    this.gameController = gameController;
    this.entityLeftContact = entityLeftContact;
    this.entityRightContact = entityRightContact;
    this.magnitude = magnitude;
  }

  @Override
  public void apply(Body body) {
    //    if (magnitude < 0) {
    //      gameController.updateEntityAttribute();
    //    }
    //    if (magnitude > 0) {
    //      //
    //    }
    body.setLinearVelocity(new Vector2(this.magnitude, body.getLinearVelocity().y));
  }

  @Override
  public Boolean isValid(Entity entity) throws ChunkNotFound {
    if (magnitude < 0) {
      return entityLeftContact.isLeftSpace(entity);
    }
    if (magnitude > 0) {
      return entityRightContact.isRightSpace(entity);
    }
    return true;
  }
}

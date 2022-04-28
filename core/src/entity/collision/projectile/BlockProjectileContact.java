package entity.collision.projectile;

import app.GameController;
import com.google.inject.Inject;
import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;

public class BlockProjectileContact implements ContactWrapper {

  @Inject GameController gameController;

  @Override
  public void beginContact(CollisionPoint source, CollisionPoint target) {
    System.out.println("BLOCK PROJECTILE HIT: " + target.getEntity());
    gameController.removeEntity(source.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

package entity.collision.projectile;

import app.GameController;
import com.google.inject.Inject;
import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;

public class EntityProjectileContact implements ContactWrapper {

  @Inject GameController gameController;

  @Override
  public void beginContact(CollisionPoint source, CollisionPoint target) {
    System.out.println("ENTITY PROJECTILE HIT");
    gameController.removeEntity(source.getEntity().getUuid());
  }

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

package entity.collision.projectile;

import entity.collision.CollisionPoint;
import entity.collision.ContactWrapper;

public class BlockProjectileContact implements ContactWrapper {

  @Override
  public void beginContact(CollisionPoint source, CollisionPoint target) {}

  @Override
  public void endContact(CollisionPoint source, CollisionPoint target) {}
}

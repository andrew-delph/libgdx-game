package infra.entity.collision.contact;

import infra.entity.Entity;

public class EntityGroundContact implements ContactWrapper {
  @Override
  public void beginContact(Object source, Object target) {
    Entity entitySource = (Entity) source;
    entitySource.increaseGroundContact();
  }

  @Override
  public void endContact(Object source, Object target) {
    Entity entitySource = (Entity) source;
    entitySource.decreaseGroundContact();
  }
}

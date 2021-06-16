package infra.entity.collision.contact;

import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.block.Block;
import infra.entity.block.DirtBlock;
import infra.entity.block.StoneBlock;
import infra.entity.collision.CollisionPair;
import infra.entity.collision.CollisionService;

public class EntityGroundContact implements ContactWrapper {

  @Inject CollisionService collisionService;

  @Inject
  public EntityGroundContact() {}

  public void beginContact(Object source, Object target) {
    Entity entitySource = (Entity) source;
    entitySource.increaseGroundContact();
  }

  public void endContact(Object source, Object target) {
    Entity entitySource = (Entity) source;
    entitySource.decreaseGroundContact();
  }

  @Override
  public void init() {
    collisionService.addCollisionConsumer(new CollisionPair(Entity.class, Block.class), this);
    collisionService.addCollisionConsumer(new CollisionPair(Entity.class, DirtBlock.class), this);
    collisionService.addCollisionConsumer(new CollisionPair(Entity.class, StoneBlock.class), this);
  }
}

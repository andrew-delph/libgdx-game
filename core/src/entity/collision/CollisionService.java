package entity.collision;

import com.google.inject.Inject;
import entity.collision.ground.EntityGroundContact;
import entity.collision.ladder.EntityLadderContact;
import entity.collision.left.EntityLeftContact;
import entity.collision.right.EntityRightContact;

import java.util.HashMap;
import java.util.Map;

public class CollisionService {
  Map<CollisionPair, ContactWrapper> collisionPairContactWrapperMap;

  @Inject EntityGroundContact entityGroundContact;

  @Inject EntityLadderContact entityLadderContact;

  @Inject EntityLeftContact entityLeftContact;

  @Inject EntityRightContact entityRightContact;

  @Inject
  public CollisionService() {
    this.collisionPairContactWrapperMap = new HashMap<>();
  }

  public void init() {
    entityGroundContact.init();
    entityLadderContact.init();
    entityLeftContact.init();
    entityRightContact.init();
  }

  public void addCollisionConsumer(CollisionPair collisionPair, ContactWrapper contactWrapper) {
    this.collisionPairContactWrapperMap.put(collisionPair, contactWrapper);
  }

  public void handleBeginCollision(CollisionPair collisionPair, Object source, Object target) {
    if (this.collisionPairContactWrapperMap.get(collisionPair) != null) {
      this.collisionPairContactWrapperMap.get(collisionPair).beginContact(source, target);
      return;
    }
    CollisionPair collisionPairReversed = collisionPair.reverse();
    if (this.collisionPairContactWrapperMap.get(collisionPairReversed) != null) {
      this.collisionPairContactWrapperMap.get(collisionPairReversed).beginContact(target, source);
    }
  }

  public void handleEndCollision(CollisionPair collisionPair, Object source, Object target) {
    if (this.collisionPairContactWrapperMap.get(collisionPair) != null) {
      this.collisionPairContactWrapperMap.get(collisionPair).endContact(source, target);
      return;
    }
    CollisionPair collisionPairReversed = collisionPair.reverse();
    if (this.collisionPairContactWrapperMap.get(collisionPairReversed) != null) {
      this.collisionPairContactWrapperMap.get(collisionPairReversed).endContact(target, source);
    }
  }
}

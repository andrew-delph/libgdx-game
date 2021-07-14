package infra.entity.collision;

import com.google.inject.Inject;
import infra.entity.collision.ground.EntityGroundContact;
import infra.entity.collision.ladder.EntityLadderContact;

import java.util.HashMap;
import java.util.Map;

public class CollisionService {
  Map<CollisionPair, ContactWrapper> collisionPairContactWrapperMap;

  @Inject EntityGroundContact entityGroundContact;

  @Inject EntityLadderContact entityLadderContact;

  @Inject
  public CollisionService() {
    this.collisionPairContactWrapperMap = new HashMap<>();
  }

  public void init() {
    entityGroundContact.init();
    entityLadderContact.init();
  }

  public void addCollisionConsumer(CollisionPair collisionPair, ContactWrapper contactWrapper) {
    this.collisionPairContactWrapperMap.put(collisionPair, contactWrapper);
  }

  public void handleBeginCollision(CollisionPair collisionPair, Object source, Object target) {
    ContactWrapper contactWrapper = this.collisionPairContactWrapperMap.get(collisionPair);
    if (contactWrapper == null) {
      return;
    }
    contactWrapper.beginContact(source, target);
  }

  public void handleEndCollision(CollisionPair collisionPair, Object source, Object target) {
    ContactWrapper contactWrapper = this.collisionPairContactWrapperMap.get(collisionPair);
    if (contactWrapper == null) return;
    contactWrapper.endContact(source, target);
  }
}

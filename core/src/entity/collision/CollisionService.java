package entity.collision;

import com.google.inject.Inject;
import entity.collision.ground.EntityGroundContact;
import entity.collision.ground.GroundPoint;
import entity.collision.ground.GroundSensorPoint;
import entity.collision.ladder.EntityLadderContact;
import entity.collision.ladder.LadderPoint;
import entity.collision.left.EntityLeftContact;
import entity.collision.left.LeftSensorPoint;
import entity.collision.right.EntityRightContact;
import entity.collision.right.RightSensorPoint;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CollisionService {
  final Logger LOGGER = LogManager.getLogger();
  Map<CollisionPair, ContactWrapperCounter> collisionPairContactWrapperMap;
  @Inject EntityGroundContact entityGroundContact;
  @Inject EntityLadderContact entityLadderContact;
  @Inject EntityLeftContact entityLeftContact;
  @Inject EntityRightContact entityRightContact;

  @Inject
  public CollisionService() {
    this.collisionPairContactWrapperMap = new HashMap<>();
  }

  public void init() {
    this.addCollisionConsumer(
        new CollisionPair(GroundSensorPoint.class, GroundPoint.class), entityGroundContact);
    this.addCollisionConsumer(
        new CollisionPair(EntityPoint.class, LadderPoint.class), entityLadderContact);
    this.addCollisionConsumer(
        new CollisionPair(LeftSensorPoint.class, GroundPoint.class), entityLeftContact);
    this.addCollisionConsumer(
        new CollisionPair(RightSensorPoint.class, GroundPoint.class), entityRightContact);
  }

  public void addCollisionConsumer(
      CollisionPair collisionPair, ContactWrapperCounter contactWrapper) {
    this.collisionPairContactWrapperMap.put(collisionPair, contactWrapper);
  }

  public void handleBeginCollision(
      CollisionPair collisionPair, CollisionPoint source, CollisionPoint target) {
    if (this.collisionPairContactWrapperMap.get(collisionPair) != null) {
      this.collisionPairContactWrapperMap.get(collisionPair).beginContact(source, target);
      return;
    }
    CollisionPair collisionPairReversed = collisionPair.reverse();
    if (this.collisionPairContactWrapperMap.get(collisionPairReversed) != null) {
      this.collisionPairContactWrapperMap.get(collisionPairReversed).beginContact(target, source);
    }
  }

  public void handleEndCollision(
      CollisionPair collisionPair, CollisionPoint source, CollisionPoint target) {
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

package entity.collision;

import com.google.inject.Inject;
import entity.collision.ladder.EntityLadderContact;
import entity.collision.ladder.LadderSensor;
import entity.collision.left.EntityLeftContact;
import entity.collision.left.LeftSensor;
import entity.collision.orb.OrbContact;
import entity.collision.orb.OrbSensor;
import entity.collision.projectile.ProjectileContact;
import entity.collision.projectile.ProjectileSensor;
import entity.collision.right.EntityRightContact;
import entity.collision.right.RightSensor;
import entity.collision.right.ground.EntityFeetSensor;
import entity.collision.right.ground.EntityGroundContact;
import entity.collision.right.ground.GroundSensor;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CollisionService {
  final Logger LOGGER = LogManager.getLogger();
  Map<CollisionPair, ContactWrapper> collisionPairContactWrapperMap;
  @Inject EntityGroundContact entityGroundContact;
  @Inject EntityLadderContact entityLadderContact;
  @Inject EntityLeftContact entityLeftContact;
  @Inject EntityRightContact entityRightContact;
  @Inject ProjectileContact projectileContact;
  @Inject OrbContact orbContact;

  @Inject
  public CollisionService() {
    this.collisionPairContactWrapperMap = new HashMap<>();
  }

  public void init() {
    this.addCollisionConsumer(
        new CollisionPair(EntityFeetSensor.class, GroundSensor.class), entityGroundContact);
    this.addCollisionConsumer(
        new CollisionPair(EntitySensor.class, LadderSensor.class), entityLadderContact);
    this.addCollisionConsumer(
        new CollisionPair(LeftSensor.class, GroundSensor.class), entityLeftContact);
    this.addCollisionConsumer(
        new CollisionPair(RightSensor.class, GroundSensor.class), entityRightContact);
    this.addCollisionConsumer(
        new CollisionPair(ProjectileSensor.class, GroundSensor.class), projectileContact);
    this.addCollisionConsumer(
        new CollisionPair(ProjectileSensor.class, EntitySensor.class), projectileContact);
    this.addCollisionConsumer(new CollisionPair(OrbSensor.class, EntitySensor.class), orbContact);
  }

  public void addCollisionConsumer(CollisionPair collisionPair, ContactWrapper contactWrapper) {
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

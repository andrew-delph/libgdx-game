package core.entity.collision;

import com.google.inject.Inject;
import core.entity.collision.ground.EntityFeetSensor;
import core.entity.collision.ground.EntityGroundContact;
import core.entity.collision.ground.GroundSensor;
import core.entity.collision.ladder.EntityLadderContact;
import core.entity.collision.ladder.LadderSensor;
import core.entity.collision.left.EntityLeftContact;
import core.entity.collision.left.LeftSensor;
import core.entity.collision.orb.OrbContact;
import core.entity.collision.orb.OrbSensor;
import core.entity.collision.projectile.ProjectileContact;
import core.entity.collision.projectile.ProjectileSensor;
import core.entity.collision.right.EntityRightContact;
import core.entity.collision.right.RightSensor;
import java.util.HashMap;
import java.util.Map;

public class CollisionService {

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
    projectileContact.init();
    this.addCollisionConsumer(new CollisionPair(OrbSensor.class, EntitySensor.class), orbContact);
    orbContact.init();
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

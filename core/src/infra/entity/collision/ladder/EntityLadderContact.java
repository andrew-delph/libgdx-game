package infra.entity.collision.ladder;

import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import infra.entity.collision.CollisionPair;
import infra.entity.collision.CollisionService;
import infra.entity.collision.ContactWrapper;
import infra.entity.collision.EntityPoint;

import java.util.HashMap;
import java.util.Map;

public class EntityLadderContact implements ContactWrapper {

  @Inject CollisionService collisionService;

  Map<Body, Integer> ladderContactCounter = new HashMap<>();

  @Override
  public void beginContact(Object source, Object target) {
    EntityPoint entityPoint = (EntityPoint) source;
    this.ladderContactCounter.putIfAbsent(entityPoint.getBody(), 0);
    int ladderCount = this.ladderContactCounter.get(entityPoint.getBody());
    this.ladderContactCounter.put(entityPoint.getBody(), ladderCount + 1);
  }

  @Override
  public void endContact(Object source, Object target) {
    EntityPoint entityPoint = (EntityPoint) source;
    this.ladderContactCounter.putIfAbsent(entityPoint.getBody(), 0);
    int ladderCount = this.ladderContactCounter.get(entityPoint.getBody());
    this.ladderContactCounter.put(entityPoint.getBody(), ladderCount - 1);
  }

  public boolean isOnLadder(Body body) {
    if (this.ladderContactCounter.get(body) == null) {
      return false;
    } else return this.ladderContactCounter.get(body) > 0;
  }

  @Override
  public void init() {
    collisionService.addCollisionConsumer(
        new CollisionPair(EntityPoint.class, LadderPoint.class), this);
  }
}

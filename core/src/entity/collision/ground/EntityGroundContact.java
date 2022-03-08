package entity.collision.ground;

import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import entity.collision.CollisionPair;
import entity.collision.CollisionService;
import entity.collision.ContactWrapper;

import java.util.HashMap;
import java.util.Map;

public class EntityGroundContact implements ContactWrapper {

  @Inject CollisionService collisionService;

  Map<Body, Integer> groundContactCounter = new HashMap<>();

  @Inject
  public EntityGroundContact() {}

  public synchronized void beginContact(Object source, Object target) {
    GroundSensorPoint groundSensorPoint = (GroundSensorPoint) source;
    this.groundContactCounter.putIfAbsent(groundSensorPoint.getBody(), 0);
    int groundCount = this.groundContactCounter.get(groundSensorPoint.getBody());
    this.groundContactCounter.put(groundSensorPoint.getBody(), groundCount + 1);
  }

  public synchronized void endContact(Object source, Object target) {
    GroundSensorPoint groundSensorPoint = (GroundSensorPoint) source;
    this.groundContactCounter.putIfAbsent(groundSensorPoint.getBody(), 0);
    int groundCount = this.groundContactCounter.get(groundSensorPoint.getBody());
    this.groundContactCounter.put(groundSensorPoint.getBody(), groundCount - 1);
  }

  public Boolean isOnGround(Body body) {
    if (this.groundContactCounter.get(body) == null) {
      return false;
    } else return this.groundContactCounter.get(body) > 0;
  }

  @Override
  public void init() {
    collisionService.addCollisionConsumer(
        new CollisionPair(GroundSensorPoint.class, GroundPoint.class), this);
  }
}

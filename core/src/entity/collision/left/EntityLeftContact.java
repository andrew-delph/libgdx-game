package entity.collision.left;

import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import entity.collision.CollisionPair;
import entity.collision.CollisionService;
import entity.collision.ContactWrapper;
import entity.collision.ground.GroundPoint;
import java.util.HashMap;
import java.util.Map;

public class EntityLeftContact implements ContactWrapper {
  @Inject CollisionService collisionService;

  Map<Body, Integer> leftContactCounter = new HashMap<>();

  @Override
  public void beginContact(Object source, Object target) {
    LeftSensorPoint leftPoint = (LeftSensorPoint) source;
    this.leftContactCounter.putIfAbsent(leftPoint.getBody(), 0);
    int leftCount = this.leftContactCounter.get(leftPoint.getBody());
    this.leftContactCounter.put(leftPoint.getBody(), leftCount + 1);
  }

  @Override
  public void endContact(Object source, Object target) {
    LeftSensorPoint leftPoint = (LeftSensorPoint) source;
    this.leftContactCounter.putIfAbsent(leftPoint.getBody(), 0);
    int leftCount = this.leftContactCounter.get(leftPoint.getBody());
    this.leftContactCounter.put(leftPoint.getBody(), leftCount - 1);
  }

  public boolean isLeftSpace(Body body) {
    if (this.leftContactCounter.get(body) == null) {
      return true;
    } else return this.leftContactCounter.get(body) == 0;
  }

  @Override
  public void init() {
    collisionService.addCollisionConsumer(
        new CollisionPair(LeftSensorPoint.class, GroundPoint.class), this);
  }
}

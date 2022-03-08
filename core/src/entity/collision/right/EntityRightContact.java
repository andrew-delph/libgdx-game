package entity.collision.right;

import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import entity.collision.CollisionPair;
import entity.collision.CollisionService;
import entity.collision.ContactWrapper;
import entity.collision.ground.GroundPoint;

import java.util.HashMap;
import java.util.Map;

public class EntityRightContact implements ContactWrapper {
  @Inject CollisionService collisionService;

  Map<Body, Integer> rightContactCounter = new HashMap<>();

  @Override
  public void beginContact(Object source, Object target) {
    RightSensorPoint rightPoint = (RightSensorPoint) source;
    this.rightContactCounter.putIfAbsent(rightPoint.getBody(), 0);
    int rightCount = this.rightContactCounter.get(rightPoint.getBody());
    this.rightContactCounter.put(rightPoint.getBody(), rightCount + 1);
  }

  @Override
  public void endContact(Object source, Object target) {
    RightSensorPoint rightPoint = (RightSensorPoint) source;
    this.rightContactCounter.putIfAbsent(rightPoint.getBody(), 0);
    int rightCount = this.rightContactCounter.get(rightPoint.getBody());
    this.rightContactCounter.put(rightPoint.getBody(), rightCount - 1);
  }

  public boolean isRightSpace(Body body) {
    if (this.rightContactCounter.get(body) == null) {
      return true;
    } else return this.rightContactCounter.get(body) == 0;
  }

  @Override
  public void init() {
    collisionService.addCollisionConsumer(
        new CollisionPair(RightSensorPoint.class, GroundPoint.class), this);
  }
}

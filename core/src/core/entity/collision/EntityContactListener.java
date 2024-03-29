package core.entity.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.google.inject.Inject;

public class EntityContactListener implements ContactListener {

  CollisionService collisionService;

  @Inject
  public EntityContactListener(CollisionService collisionService) {
    this.collisionService = collisionService;
  }

  public void beginContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();
    CollisionPoint objectA = (CollisionPoint) fixtureA.getUserData();
    CollisionPoint objectB = (CollisionPoint) fixtureB.getUserData();
    if (objectA == null || objectB == null) return;
    this.collisionService.handleBeginCollision(
        new CollisionPair(objectA.getClass(), objectB.getClass()), objectA, objectB);
  }

  @Override
  public void endContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();
    CollisionPoint objectA = (CollisionPoint) fixtureA.getUserData();
    CollisionPoint objectB = (CollisionPoint) fixtureB.getUserData();
    if (objectA == null || objectB == null) return;
    this.collisionService.handleEndCollision(
        new CollisionPair(objectA.getClass(), objectB.getClass()), objectA, objectB);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}
}

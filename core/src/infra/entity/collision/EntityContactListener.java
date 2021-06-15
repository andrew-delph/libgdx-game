package infra.entity.collision;

import com.badlogic.gdx.physics.box2d.*;
import com.google.inject.Inject;

public class EntityContactListener implements ContactListener {

  @Inject CollisionService collisionService;

  public EntityContactListener() {
    System.out.println("create EntityContactListener");
  }

  public void beginContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();
    System.out.println(
        "beginContact  between " + fixtureA.getUserData() + " and " + fixtureB.getUserData());
  }

  @Override
  public void endContact(Contact contact) {}

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}
}

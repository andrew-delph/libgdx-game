package core.entity.collision;

public interface ContactWrapper {
  void beginContact(CollisionPoint source, CollisionPoint target);

  void endContact(CollisionPoint source, CollisionPoint target);
}

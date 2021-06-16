package infra.entity.collision.contact;

public abstract interface ContactWrapper {
  void beginContact(Object source, Object target);

  void endContact(Object source, Object target);

  void init();
}

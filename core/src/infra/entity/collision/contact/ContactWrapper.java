package infra.entity.collision.contact;

public interface ContactWrapper {
  void beginContact(Object source, Object target);

  void endContact(Object source, Object target);
}

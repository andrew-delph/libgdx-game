package entity.collision;

public interface ContactWrapper {
  void beginContact(Object source, Object target);

  void endContact(Object source, Object target);

  void init();
}

package entity.collision;

import common.exceptions.BodyNotFound;

public interface ContactWrapper {
  void beginContact(Object source, Object target) throws BodyNotFound;

  void endContact(Object source, Object target) throws BodyNotFound;

  void init();
}

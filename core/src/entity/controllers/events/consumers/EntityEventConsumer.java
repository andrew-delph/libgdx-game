package entity.controllers.events.consumers;

import entity.controllers.events.types.AbstractEntityEventType;

public interface EntityEventConsumer {
  void accept(AbstractEntityEventType entityEvent);
}

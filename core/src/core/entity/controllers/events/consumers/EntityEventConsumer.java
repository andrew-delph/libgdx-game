package core.entity.controllers.events.consumers;

import core.entity.controllers.events.types.AbstractEntityEventType;

public interface EntityEventConsumer {
  void accept(AbstractEntityEventType entityEvent);
}

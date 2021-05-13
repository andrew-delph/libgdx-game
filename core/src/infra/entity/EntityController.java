package infra.entity;

import com.google.inject.Inject;
import infra.events.EventService;
import networking.events.outgoing.OutgoingUpdateEntityEvent;

public class EntityController {

  @Inject EventService eventService;

  public void moveX(Entity entity, int move) {
    entity.moveX(move);
    eventService.fireEvent(new OutgoingUpdateEntityEvent(entity.toEntityData()));
  }

  public void moveY(Entity entity, int move) {
    entity.moveY(move);
    eventService.fireEvent(new OutgoingUpdateEntityEvent(entity.toEntityData()));
  }
}

package old.infra.entity;

import com.google.inject.Inject;
import old.infra.events.EventService;
import old.networking.events.outgoing.OutgoingUpdateEntityEvent;

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

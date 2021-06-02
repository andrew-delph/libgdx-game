package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;

import java.util.UUID;

public class DisconnectionEvent extends Event {

  public static String type = "disconnection_entity_incoming";
  UUID uuid;

  @Inject
  public DisconnectionEvent(@Assisted UUID uuid) {
    this.uuid = uuid;
  }

  public UUID getUuid() {
    return uuid;
  }

  @Override
  public String getType() {
    return type;
  }
}

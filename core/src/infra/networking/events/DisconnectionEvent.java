package infra.networking.events;

import com.google.inject.Inject;

import java.util.UUID;

import infra.common.events.Event;

public class DisconnectionEvent extends Event {

  public static String type = "disconnection_entity_incoming";
  UUID uuid;

  @Inject
  public DisconnectionEvent(UUID uuid) {
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

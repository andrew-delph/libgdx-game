package networking.events.types.incoming;

import com.google.inject.Inject;
import common.events.EventType;

import java.util.UUID;

public class DisconnectionIncomingEventType extends EventType {

  public static String type = "disconnection_entity_incoming";
  UUID uuid;

  @Inject
  public DisconnectionIncomingEventType(UUID uuid) {
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

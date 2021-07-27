package networking.events;

import com.google.inject.Inject;
import common.events.Event;
import networking.NetworkObjects;

import java.util.UUID;

public class CreateEntityIncomingEvent extends Event {

  public static String type = "create_entity_incoming";

  public NetworkObjects.NetworkEvent networkEvent;

  @Inject
  public CreateEntityIncomingEvent(NetworkObjects.NetworkEvent networkEvent) {
    this.networkEvent = networkEvent;
  }

  public NetworkObjects.NetworkData getData() {
    return this.networkEvent.getData();
  }

  public UUID getUser() {
    return UUID.fromString(this.networkEvent.getUser());
  }

  @Override
  public String getType() {
    return type;
  }
}

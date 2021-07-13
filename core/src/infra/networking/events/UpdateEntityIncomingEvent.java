package infra.networking.events;

import com.google.inject.Inject;

import java.util.UUID;

import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class UpdateEntityIncomingEvent extends Event {

  public static String type = "update_entity_incoming";

  public NetworkObjects.NetworkEvent networkEvent;

  @Inject
  UpdateEntityIncomingEvent(NetworkObjects.NetworkEvent networkEvent) {
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

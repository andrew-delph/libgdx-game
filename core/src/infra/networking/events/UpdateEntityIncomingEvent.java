package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

import java.util.UUID;

public class UpdateEntityIncomingEvent extends Event {

  public static String type = "update_entity_incoming";

  public NetworkObjects.NetworkEvent networkEvent;

  @Inject
  UpdateEntityIncomingEvent(@Assisted NetworkObjects.NetworkEvent networkEvent) {
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
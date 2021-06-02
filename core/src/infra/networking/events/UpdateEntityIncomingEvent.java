package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class UpdateEntityIncomingEvent extends Event {

  public static String type = "update_entity_incoming";

  NetworkObjects.NetworkEvent networkEvent;

  @Inject
  UpdateEntityIncomingEvent(@Assisted NetworkObjects.NetworkEvent networkEvent) {
    this.networkEvent = networkEvent;
  }

  public NetworkObjects.NetworkData getData() {
    return this.networkEvent.getData();
  }

  @Override
  public String getType() {
    return type;
  }
}

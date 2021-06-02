package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class CreateEntityIncomingEvent extends Event {

  public static String type = "create_entity_incoming";

  public NetworkObjects.NetworkEvent networkEvent;

  @Inject
  public CreateEntityIncomingEvent(@Assisted NetworkObjects.NetworkEvent networkEvent) {
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

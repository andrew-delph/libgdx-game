package networking.events;

import com.google.inject.Inject;
import common.events.Event;
import networking.NetworkObjects;

public class RemoveEntityIncomingEvent extends Event {

  public static String type = "remove_entity_incoming";

  public NetworkObjects.NetworkEvent networkEvent;

  @Inject
  RemoveEntityIncomingEvent(NetworkObjects.NetworkEvent networkEvent) {
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

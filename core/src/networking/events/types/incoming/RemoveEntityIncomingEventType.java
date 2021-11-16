package networking.events.types.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;

public class RemoveEntityIncomingEventType extends EventType {

  public static String type = "remove_entity_incoming";

  public NetworkObjects.NetworkEvent networkEvent;

  @Inject
  public RemoveEntityIncomingEventType(NetworkObjects.NetworkEvent networkEvent) {
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

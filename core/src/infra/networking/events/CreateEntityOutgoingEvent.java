package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class CreateEntityOutgoingEvent extends Event {

  NetworkObjects.NetworkData entityData;

  public static String type = "create_entity_outgoing";

  @Inject
  CreateEntityOutgoingEvent(@Assisted NetworkObjects.NetworkData entityData) {
    this.entityData = entityData;
  }

  public NetworkObjects.NetworkEvent getNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder().setData(this.entityData).setEvent(type).build();
  }

  public String getType() {
    return type;
  }
}

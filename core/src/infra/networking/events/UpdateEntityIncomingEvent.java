package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class UpdateEntityIncomingEvent extends Event {

  public static String type = "update_entity_incoming";

  NetworkObjects.NetworkData entityData;

  @Inject
  UpdateEntityIncomingEvent(@Assisted NetworkObjects.NetworkData entityData) {
    this.entityData = entityData;
  }

  public NetworkObjects.NetworkData getData() {
    return this.entityData;
  }

  @Override
  public String getType() {
    return type;
  }
}

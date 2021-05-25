package infra.networking.events;

import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class CreateEntityIncomingEvent extends Event {

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return null;
  }

  @Override
  public String getType() {
    return null;
  }
}

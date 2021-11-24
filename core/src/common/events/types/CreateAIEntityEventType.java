package common.events.types;

import common.Coordinates;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

public class CreateAIEntityEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "create_ai";

  Coordinates coordinates;

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public CreateAIEntityEventType(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder().setData(this.coordinates.toNetworkData()).setEvent(type).build();
  }
}

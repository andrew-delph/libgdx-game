package common.events.types;

import static networking.translation.NetworkDataSerializer.serializeCreateTurretEventType;

import entity.attributes.Coordinates;
import networking.NetworkObjects.NetworkEvent;
import networking.events.interfaces.SerializeNetworkEvent;

public class CreateTurretEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "create_turret";
  Coordinates coordinates;

  public CreateTurretEventType(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public NetworkEvent toNetworkEvent() {
    return serializeCreateTurretEventType(this);
  }
}

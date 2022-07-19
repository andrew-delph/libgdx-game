package core.common.events.types;

import static core.networking.translation.NetworkDataSerializer.serializeCreateTurretEventType;

import core.entity.attributes.msc.Coordinates;
import core.networking.events.interfaces.SerializeNetworkEvent;
import java.util.UUID;
import networking.NetworkObjects.NetworkEvent;

public class CreateTurretEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "create_turret";
  Coordinates coordinates;
  UUID entityUUID;

  public CreateTurretEventType(UUID entityUUID, Coordinates coordinates) {
    this.coordinates = coordinates;
    this.entityUUID = entityUUID;
  }

  public UUID getEntityUUID() {
    return entityUUID;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  @Override
  public String getEventType() {
    return type;
  }

  @Override
  public NetworkEvent toNetworkEvent() {
    return serializeCreateTurretEventType(this);
  }
}

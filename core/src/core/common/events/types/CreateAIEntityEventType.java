package core.common.events.types;

import static core.networking.translation.NetworkDataSerializer.createCreateAIEntityEventType;

import core.entity.attributes.msc.Coordinates;
import core.networking.events.interfaces.SerializeNetworkEvent;
import java.util.UUID;
import networking.NetworkObjects;

public class CreateAIEntityEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "create_ai";

  Coordinates coordinates;
  UUID target;

  public CreateAIEntityEventType(Coordinates coordinates, UUID target) {
    this.coordinates = coordinates;
    this.target = target;
  }

  public UUID getTarget() {
    return target;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  @Override
  public String getEventType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return createCreateAIEntityEventType(this);
  }
}

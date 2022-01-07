package common.events.types;

import common.Coordinates;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

import java.util.UUID;

public class CreateAIEntityEventType extends EventType implements SerializeNetworkEvent {

    public static String type = "create_ai";

    Coordinates coordinates;
    UUID target;

    public UUID getTarget() {
        return target;
    }

    public CreateAIEntityEventType(Coordinates coordinates, UUID target) {
        this.coordinates = coordinates;
        this.target = target;
    }

    public Coordinates getCoordinates() {
        return coordinates;
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

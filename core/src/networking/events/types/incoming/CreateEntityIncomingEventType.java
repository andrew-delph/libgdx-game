package networking.events.types.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;

import java.util.UUID;

public class CreateEntityIncomingEventType extends EventType {

    public static String type = "create_entity_incoming";

    public NetworkObjects.NetworkEvent networkEvent;

    @Inject
    public CreateEntityIncomingEventType(NetworkObjects.NetworkEvent networkEvent) {
        this.networkEvent = networkEvent;
    }

    public NetworkObjects.NetworkData getData() {
        return this.networkEvent.getData();
    }

    public UUID getUser() {
        return UUID.fromString(this.networkEvent.getUser());
    }

    @Override
    public String getType() {
        return type;
    }
}

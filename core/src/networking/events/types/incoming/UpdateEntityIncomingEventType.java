package networking.events.types.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.NetworkObjects;

import java.util.UUID;

public class UpdateEntityIncomingEventType extends EventType {

    public static String type = "update_entity_incoming";

    public NetworkObjects.NetworkEvent networkEvent;

    @Inject
    public UpdateEntityIncomingEventType(NetworkObjects.NetworkEvent networkEvent) {
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

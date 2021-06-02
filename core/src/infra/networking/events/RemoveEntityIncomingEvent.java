package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class RemoveEntityIncomingEvent extends Event {

    public static String type = "remove_entity_incoming";

    public NetworkObjects.NetworkEvent networkEvent;

    @Inject
    RemoveEntityIncomingEvent(@Assisted NetworkObjects.NetworkEvent networkEvent){
        this.networkEvent = networkEvent;
    }

    public NetworkObjects.NetworkData getData() {
        return this.networkEvent.getData();
    }

    @Override
    public String getType() {
        return type;
    }
}
